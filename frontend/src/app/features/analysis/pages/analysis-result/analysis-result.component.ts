import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

import { AnalysisResponse } from '../../models/analysis-response.model';
import { AnalysisRequest } from '../../models/analysis-request.model';
@Component({
  selector: 'app-analysis-result',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './analysis-result.component.html',
  styleUrl: './analysis-result.component.scss'
})
export class AnalysisResultComponent {

  private router = inject(Router);

  result!: AnalysisResponse;
  request!: AnalysisRequest;

    constructor() {

    const state = this.router.getCurrentNavigation()?.extras.state;

    if (!state) {

        this.router.navigate(['/']);

        return;

    }

    this.result = state['result'];

    this.request = state['request'];

    }

  newAnalysis(): void {

    this.router.navigate(['/']);

  }

 generateBusinessPlan() {

  this.router.navigate(
    ['/business-plan-redirect'],
    {
      state: {

        result: this.result,

        request: this.request

      }
    }
  );

}
goToHistory(): void {
  this.router.navigate(['/history']);
}

}