import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import {
  BusinessPlanTransferRequest,
  BusinessPlanTransferService
} from '../../core/services/business-plan-transfer';

@Component({
  selector: 'app-analysis-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './analysis-form.component.html',
  styleUrl: './analysis-form.component.scss'
})
export class AnalysisFormComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly transferService = inject(BusinessPlanTransferService);

  isSubmitting = false;
  errorMessage = '';

  readonly form = this.formBuilder.nonNullable.group({
    projectName: ['Business Plan Draft', Validators.required],
    companyName: ['3LM Solutions', Validators.required],
    sector: ['Technology', Validators.required],
    analysisSummary: [
      'Initial market analysis prepared for the business plan generation flow.',
      Validators.required
    ],
    investmentAmount: [10000, [Validators.required, Validators.min(0)]],
    targetReturn: [15, [Validators.required, Validators.min(0)]],
    riskLevel: ['Moyen', Validators.required]
  });

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
        this.errorMessage = 'Unable to generate the business plan redirect.';
      }
    });
  }
}