import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { ProjectSectionComponent } from '../../components/project-section/project-section.component';
import { FinanceSectionComponent } from '../../components/finance-section/finance-section.component';
import { ContextSectionComponent } from '../../components/context-section/context-section.component';
import { AnalysisService } from '../../../../features/analysis/services/analysis.service';
import { AnalysisRequest } from '../../models/analysis-request.model';
import { AnalysisResponse } from '../../models/analysis-response.model';
import { Router } from '@angular/router';
@Component({
  selector: 'app-analysis-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ProjectSectionComponent,
    FinanceSectionComponent,
    ContextSectionComponent
  ],
  templateUrl: './analysis-form.component.html',
  styleUrl: './analysis-form.component.scss'
})
export class AnalysisFormComponent {

  private fb = inject(FormBuilder).nonNullable;
  private analysisService = inject(AnalysisService);
  private router = inject(Router);
  currentStep = 1;

readonly analysisForm = this.fb.group({

  // ===== Projet =====

  sector: [
    '',
    [
      Validators.required,
      Validators.minLength(3)
    ]
  ],

  region: [
    '',
    Validators.required
  ],

  description: [
    '',
    [
      Validators.required,
      Validators.minLength(20)
    ]
  ],

  stage: [
    '',
    Validators.required
  ],

  // ===== Finance =====

  initialBudget: [
    0,
    [
      Validators.required,
      Validators.min(1000)
    ]
  ],

  financingSource: [
    '',
    Validators.required
  ],

  expectedRevenue: [
    0,
    [
      Validators.required,
      Validators.min(0)
    ]
  ],

  expectedRoiMonths: [
    0,
    [
      Validators.required,
      Validators.min(1)
    ]
  ],

  // ===== Contexte =====

  experienceLevel: [
    '',
    Validators.required
  ],

  competitionLevel: [
    '',
    Validators.required
  ],

  taxAdvantages: [
    false
  ],

  freeZoneStatus: [
    false
  ]

});
nextStep(): void {

  const controlsByStep: Record<number, string[]> = {

    1: [
      'sector',
      'region',
      'description',
      'stage'
    ],

    2: [
      'initialBudget',
      'financingSource',
      'expectedRevenue',
      'expectedRoiMonths'
    ],

    3: [
      'experienceLevel',
      'competitionLevel'
    ]

  };

  const controls = controlsByStep[this.currentStep];

  if (!controls) {
    return;
  }

  controls.forEach(name => {

    const control = this.analysisForm.get(name);

    control?.markAsTouched();

    control?.updateValueAndValidity();

  });

  const hasErrors = controls.some(name =>
    this.analysisForm.get(name)?.invalid
  );

  if (hasErrors) {
    return;
  }

  this.currentStep++;

}
previousStep(): void {

  if (this.currentStep > 1) {

    this.currentStep--;

  }

}
submit(): void {

  this.analysisForm.markAllAsTouched();

  if (this.analysisForm.invalid) {

    return;

  }

  const request: AnalysisRequest =
    this.analysisForm.getRawValue() as AnalysisRequest;

  this.analysisService
      .analyze(request)
      .subscribe({

        next: (response) => {

          this.router.navigate(
            ['/analysis-result'],
            {
              state: {

                result: response,

                request: this.analysisForm.getRawValue()

              }
            }
          );

        },

        error: err => {

          console.error(err);

        }

      });

}


}