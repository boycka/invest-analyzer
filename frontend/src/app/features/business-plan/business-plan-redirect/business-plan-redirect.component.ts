import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import {
  BusinessPlanTransferRequest,
  BusinessPlanTransferService
} from '../../../core/services/business-plan-transfer';

@Component({
  selector: 'app-business-plan-redirect',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './business-plan-redirect.component.html',
  styleUrl: './business-plan-redirect.component.scss'
})
export class BusinessPlanRedirectComponent {

  private readonly formBuilder = inject(FormBuilder);
  private readonly transferService = inject(BusinessPlanTransferService);
  private readonly router = inject(Router);

  isSubmitting = false;
  errorMessage = '';

  readonly form = this.formBuilder.nonNullable.group({

    // Ces deux champs seront saisis par l'utilisateur
    projectName: ['', Validators.required],

    companyName: ['', Validators.required],

    // Ces champs seront remplis automatiquement
    sector: ['', Validators.required],

    analysisSummary: ['', Validators.required],

    investmentAmount: [0, [Validators.required, Validators.min(0)]],

    targetReturn: [0, [Validators.required, Validators.min(0)]],

    riskLevel: ['', Validators.required]

  });

  constructor() {

    const state = this.router.getCurrentNavigation()?.extras.state;

    if (!state) {
      return;
    }

    const request = state['request'];
    const result = state['result'];

    if (!request || !result) {
      return;
    }

    this.form.patchValue({

      sector: request.sector,

      analysisSummary: result.analysisSummary,

      investmentAmount: request.initialBudget,

      targetReturn: request.expectedRevenue,

      riskLevel: result.recommendation

    });

  }

  submit(): void {

    if (this.form.invalid || this.isSubmitting) {

      this.form.markAllAsTouched();

      return;

    }

    this.isSubmitting = true;

    this.errorMessage = '';

    const payload: BusinessPlanTransferRequest = this.form.getRawValue();

    this.transferService.transferToBusinessPlan(payload).subscribe({

      next: (response) => {

        window.location.assign(response.redirectUrl);

      },

      error: () => {

        this.isSubmitting = false;

        this.errorMessage =
          'Impossible de transférer les données vers le Business Plan.';

      }

    });

  }

}