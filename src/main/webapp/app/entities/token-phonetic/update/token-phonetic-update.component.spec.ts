import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ITrademarkToken } from 'app/entities/trademark-token/trademark-token.model';
import { TrademarkTokenService } from 'app/entities/trademark-token/service/trademark-token.service';
import { TokenPhoneticService } from '../service/token-phonetic.service';
import { ITokenPhonetic } from '../token-phonetic.model';
import { TokenPhoneticFormService } from './token-phonetic-form.service';

import { TokenPhoneticUpdateComponent } from './token-phonetic-update.component';

describe('TokenPhonetic Management Update Component', () => {
  let comp: TokenPhoneticUpdateComponent;
  let fixture: ComponentFixture<TokenPhoneticUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tokenPhoneticFormService: TokenPhoneticFormService;
  let tokenPhoneticService: TokenPhoneticService;
  let trademarkTokenService: TrademarkTokenService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TokenPhoneticUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TokenPhoneticUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TokenPhoneticUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tokenPhoneticFormService = TestBed.inject(TokenPhoneticFormService);
    tokenPhoneticService = TestBed.inject(TokenPhoneticService);
    trademarkTokenService = TestBed.inject(TrademarkTokenService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call TrademarkToken query and add missing value', () => {
      const tokenPhonetic: ITokenPhonetic = { id: 920 };
      const trademarkToken: ITrademarkToken = { id: 1043 };
      tokenPhonetic.trademarkToken = trademarkToken;

      const trademarkTokenCollection: ITrademarkToken[] = [{ id: 1043 }];
      jest.spyOn(trademarkTokenService, 'query').mockReturnValue(of(new HttpResponse({ body: trademarkTokenCollection })));
      const additionalTrademarkTokens = [trademarkToken];
      const expectedCollection: ITrademarkToken[] = [...additionalTrademarkTokens, ...trademarkTokenCollection];
      jest.spyOn(trademarkTokenService, 'addTrademarkTokenToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tokenPhonetic });
      comp.ngOnInit();

      expect(trademarkTokenService.query).toHaveBeenCalled();
      expect(trademarkTokenService.addTrademarkTokenToCollectionIfMissing).toHaveBeenCalledWith(
        trademarkTokenCollection,
        ...additionalTrademarkTokens.map(expect.objectContaining),
      );
      expect(comp.trademarkTokensSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const tokenPhonetic: ITokenPhonetic = { id: 920 };
      const trademarkToken: ITrademarkToken = { id: 1043 };
      tokenPhonetic.trademarkToken = trademarkToken;

      activatedRoute.data = of({ tokenPhonetic });
      comp.ngOnInit();

      expect(comp.trademarkTokensSharedCollection).toContainEqual(trademarkToken);
      expect(comp.tokenPhonetic).toEqual(tokenPhonetic);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITokenPhonetic>>();
      const tokenPhonetic = { id: 24987 };
      jest.spyOn(tokenPhoneticFormService, 'getTokenPhonetic').mockReturnValue(tokenPhonetic);
      jest.spyOn(tokenPhoneticService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tokenPhonetic });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tokenPhonetic }));
      saveSubject.complete();

      // THEN
      expect(tokenPhoneticFormService.getTokenPhonetic).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tokenPhoneticService.update).toHaveBeenCalledWith(expect.objectContaining(tokenPhonetic));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITokenPhonetic>>();
      const tokenPhonetic = { id: 24987 };
      jest.spyOn(tokenPhoneticFormService, 'getTokenPhonetic').mockReturnValue({ id: null });
      jest.spyOn(tokenPhoneticService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tokenPhonetic: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tokenPhonetic }));
      saveSubject.complete();

      // THEN
      expect(tokenPhoneticFormService.getTokenPhonetic).toHaveBeenCalled();
      expect(tokenPhoneticService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITokenPhonetic>>();
      const tokenPhonetic = { id: 24987 };
      jest.spyOn(tokenPhoneticService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tokenPhonetic });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tokenPhoneticService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTrademarkToken', () => {
      it('should forward to trademarkTokenService', () => {
        const entity = { id: 1043 };
        const entity2 = { id: 17385 };
        jest.spyOn(trademarkTokenService, 'compareTrademarkToken');
        comp.compareTrademarkToken(entity, entity2);
        expect(trademarkTokenService.compareTrademarkToken).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
