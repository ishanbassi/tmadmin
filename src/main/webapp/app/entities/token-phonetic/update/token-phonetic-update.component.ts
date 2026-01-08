import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITrademarkToken } from 'app/entities/trademark-token/trademark-token.model';
import { TrademarkTokenService } from 'app/entities/trademark-token/service/trademark-token.service';
import { PhoneticAlgorithmType } from 'app/entities/enumerations/phonetic-algorithm-type.model';
import { TokenPhoneticService } from '../service/token-phonetic.service';
import { ITokenPhonetic } from '../token-phonetic.model';
import { TokenPhoneticFormGroup, TokenPhoneticFormService } from './token-phonetic-form.service';

@Component({
  selector: 'jhi-token-phonetic-update',
  templateUrl: './token-phonetic-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TokenPhoneticUpdateComponent implements OnInit {
  isSaving = false;
  tokenPhonetic: ITokenPhonetic | null = null;
  phoneticAlgorithmTypeValues = Object.keys(PhoneticAlgorithmType);

  trademarkTokensSharedCollection: ITrademarkToken[] = [];

  protected tokenPhoneticService = inject(TokenPhoneticService);
  protected tokenPhoneticFormService = inject(TokenPhoneticFormService);
  protected trademarkTokenService = inject(TrademarkTokenService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TokenPhoneticFormGroup = this.tokenPhoneticFormService.createTokenPhoneticFormGroup();

  compareTrademarkToken = (o1: ITrademarkToken | null, o2: ITrademarkToken | null): boolean =>
    this.trademarkTokenService.compareTrademarkToken(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tokenPhonetic }) => {
      this.tokenPhonetic = tokenPhonetic;
      if (tokenPhonetic) {
        this.updateForm(tokenPhonetic);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tokenPhonetic = this.tokenPhoneticFormService.getTokenPhonetic(this.editForm);
    if (tokenPhonetic.id !== null) {
      this.subscribeToSaveResponse(this.tokenPhoneticService.update(tokenPhonetic));
    } else {
      this.subscribeToSaveResponse(this.tokenPhoneticService.create(tokenPhonetic));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITokenPhonetic>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(tokenPhonetic: ITokenPhonetic): void {
    this.tokenPhonetic = tokenPhonetic;
    this.tokenPhoneticFormService.resetForm(this.editForm, tokenPhonetic);

    this.trademarkTokensSharedCollection = this.trademarkTokenService.addTrademarkTokenToCollectionIfMissing<ITrademarkToken>(
      this.trademarkTokensSharedCollection,
      tokenPhonetic.trademarkToken,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.trademarkTokenService
      .query()
      .pipe(map((res: HttpResponse<ITrademarkToken[]>) => res.body ?? []))
      .pipe(
        map((trademarkTokens: ITrademarkToken[]) =>
          this.trademarkTokenService.addTrademarkTokenToCollectionIfMissing<ITrademarkToken>(
            trademarkTokens,
            this.tokenPhonetic?.trademarkToken,
          ),
        ),
      )
      .subscribe((trademarkTokens: ITrademarkToken[]) => (this.trademarkTokensSharedCollection = trademarkTokens));
  }
}
