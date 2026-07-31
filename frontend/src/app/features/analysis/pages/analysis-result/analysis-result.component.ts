import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

import { AnalysisPdfService } from '../../services/analysis-pdf.service';
import { AnalysisResponse } from '../../models/analysis-response.model';
import { AnalysisRequest } from '../../models/analysis-request.model';
import { ScoreGaugeComponent } from '../../components/score-gauge/score-gauge.component';

@Component({
  selector: 'app-analysis-result',
  standalone: true,
  imports: [CommonModule, ScoreGaugeComponent],
  templateUrl: './analysis-result.component.html',
  styleUrl: './analysis-result.component.scss'
})
export class AnalysisResultComponent {
  private readonly router = inject(Router);
  private readonly pdfService = inject(AnalysisPdfService);

  result!: AnalysisResponse;
  request!: AnalysisRequest;

  constructor() {
    const state = this.router.getCurrentNavigation()?.extras.state;
    if (!state?.['result']) {
      this.router.navigate(['/']);
      return;
    }
    this.result = state['result'];
    this.request = state['request'];
  }

  isFallback(): boolean {
    return this.result?.generationSource === 'FALLBACK';
  }

  newAnalysis(): void { this.router.navigate(['/']); }

  generateBusinessPlan(): void {
    this.router.navigate(['/business-plan-redirect'], {
      state: { result: this.result, request: this.request }
    });
  }

  downloadPdf(): void {
    if (this.result?.id) this.pdfService.download(this.result.id);
  }

  goToHistory(): void { this.router.navigate(['/history']); }
}