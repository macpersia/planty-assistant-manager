import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPairingRequest, NewPairingRequest } from '../pairing-request.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPairingRequest for edit and NewPairingRequestFormGroupInput for create.
 */
type PairingRequestFormGroupInput = IPairingRequest | PartialWithRequiredKeyOf<NewPairingRequest>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPairingRequest | NewPairingRequest> = Omit<T, 'requestTime'> & {
  requestTime?: string | null;
};

type PairingRequestFormRawValue = FormValueOf<IPairingRequest>;

type NewPairingRequestFormRawValue = FormValueOf<NewPairingRequest>;

type PairingRequestFormDefaults = Pick<NewPairingRequest, 'id' | 'requestTime' | 'accepted'>;

type PairingRequestFormGroupContent = {
  id: FormControl<PairingRequestFormRawValue['id'] | NewPairingRequest['id']>;
  name: FormControl<PairingRequestFormRawValue['name']>;
  verificationCode: FormControl<PairingRequestFormRawValue['verificationCode']>;
  requestTime: FormControl<PairingRequestFormRawValue['requestTime']>;
  accepted: FormControl<PairingRequestFormRawValue['accepted']>;
  sessionId: FormControl<PairingRequestFormRawValue['sessionId']>;
  publicKey: FormControl<PairingRequestFormRawValue['publicKey']>;
  login: FormControl<PairingRequestFormRawValue['login']>;
};

export type PairingRequestFormGroup = FormGroup<PairingRequestFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PairingRequestFormService {
  createPairingRequestFormGroup(pairingRequest: PairingRequestFormGroupInput = { id: null }): PairingRequestFormGroup {
    const pairingRequestRawValue = this.convertPairingRequestToPairingRequestRawValue({
      ...this.getFormDefaults(),
      ...pairingRequest,
    });
    return new FormGroup<PairingRequestFormGroupContent>({
      id: new FormControl(
        { value: pairingRequestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(pairingRequestRawValue.name),
      verificationCode: new FormControl(pairingRequestRawValue.verificationCode),
      requestTime: new FormControl(pairingRequestRawValue.requestTime),
      accepted: new FormControl(pairingRequestRawValue.accepted),
      sessionId: new FormControl(pairingRequestRawValue.sessionId),
      publicKey: new FormControl(pairingRequestRawValue.publicKey),
      login: new FormControl(pairingRequestRawValue.login),
    });
  }

  getPairingRequest(form: PairingRequestFormGroup): IPairingRequest | NewPairingRequest {
    return this.convertPairingRequestRawValueToPairingRequest(
      form.getRawValue() as PairingRequestFormRawValue | NewPairingRequestFormRawValue
    );
  }

  resetForm(form: PairingRequestFormGroup, pairingRequest: PairingRequestFormGroupInput): void {
    const pairingRequestRawValue = this.convertPairingRequestToPairingRequestRawValue({ ...this.getFormDefaults(), ...pairingRequest });
    form.reset(
      {
        ...pairingRequestRawValue,
        id: { value: pairingRequestRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PairingRequestFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      requestTime: currentTime,
      accepted: false,
    };
  }

  private convertPairingRequestRawValueToPairingRequest(
    rawPairingRequest: PairingRequestFormRawValue | NewPairingRequestFormRawValue
  ): IPairingRequest | NewPairingRequest {
    return {
      ...rawPairingRequest,
      requestTime: dayjs(rawPairingRequest.requestTime, DATE_TIME_FORMAT),
    };
  }

  private convertPairingRequestToPairingRequestRawValue(
    pairingRequest: IPairingRequest | (Partial<NewPairingRequest> & PairingRequestFormDefaults)
  ): PairingRequestFormRawValue | PartialWithRequiredKeyOf<NewPairingRequestFormRawValue> {
    return {
      ...pairingRequest,
      requestTime: pairingRequest.requestTime ? pairingRequest.requestTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
