import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ProjectSectionComponent } from '../../components/project-section/project-section.component';
import { FinanceSectionComponent } from '../../components/finance-section/finance-section.component';
import { ContextSectionComponent } from '../../components/context-section/context-section.component';
import { AnalysisRequest } from '../../models/analysis-request.model';

@Component({
  selector: 'app-analysis-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ProjectSectionComponent, FinanceSectionComponent, ContextSectionComponent],
  templateUrl: './analysis-form.component.html',
  styleUrl: './analysis-form.component.scss'
})
export class AnalysisFormComponent {
  private readonly fb = inject(FormBuilder).nonNullable;
  private readonly router = inject(Router);
  currentStep = 1;
  isSubmitting = false;
  errorMessage = '';
  analysisPhase = '';

  readonly analysisForm = this.fb.group({
    sector: ['', [Validators.required, Validators.minLength(3)]], region: ['', Validators.required],
    description: ['', [Validators.required, Validators.minLength(20)]], stage: ['', Validators.required],
    initialBudget: [0, [Validators.required, Validators.min(1000)]], financingSource: ['', Validators.required],
    expectedRevenue: [0, [Validators.required, Validators.min(0)]], expectedRoiMonths: [0, [Validators.required, Validators.min(1)]],
    experienceLevel: ['', Validators.required], competitionLevel: ['', Validators.required],
    taxAdvantages: [false], freeZoneStatus: [false]
  });

  constructor() {
    const state = this.router.getCurrentNavigation()?.extras.state;
    if (state?.['prefill']) this.analysisForm.patchValue(state['prefill'] as Partial<AnalysisRequest>);
  }

  nextStep(): void {
    const controlsByStep: Record<number, string[]> = { 1: ['sector','region','description','stage'], 2: ['initialBudget','financingSource','expectedRevenue','expectedRoiMonths'], 3: ['experienceLevel','competitionLevel'] };
    const controls = controlsByStep[this.currentStep]; if (!controls) return;
    controls.forEach(name => { const control = this.analysisForm.get(name); control?.markAsTouched(); control?.updateValueAndValidity(); });
    if (controls.some(name => this.analysisForm.get(name)?.invalid)) return;
    this.currentStep++;
  }

  previousStep(): void { if (this.currentStep > 1) this.currentStep--; }

  submit(): void {
    this.analysisForm.markAllAsTouched();
    if (this.analysisForm.invalid) return;
    const request = this.analysisForm.getRawValue() as AnalysisRequest;
    this.router.navigate(['/payment'], { state: { request } });
  }
}