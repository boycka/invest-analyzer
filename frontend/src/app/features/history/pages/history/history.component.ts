import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { HistoryService } from '../../services/history.service';
import { History } from '../../models/history.model';

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './history.component.html',
  styleUrl: './history.component.scss'
})
export class HistoryComponent {
  private readonly historyService = inject(HistoryService);
  private readonly router = inject(Router);

  histories$ = this.historyService.getHistory();
  errorMessage = '';

  voirDetail(history: History): void {
    this.historyService.getById(history.id).subscribe({
      next: detail => this.router.navigate(['/analysis-result'], {
        state: { result: detail.result, request: detail.request }
      }),
      error: () => this.errorMessage = 'Impossible de charger le détail de cette analyse.'
    });
  }

  relancerAnalyse(history: History): void {
    this.historyService.getById(history.id).subscribe({
      next: detail => this.router.navigate(['/'], { state: { prefill: detail.request } }),
      error: () => this.errorMessage = 'Impossible de relancer cette analyse.'
    });
  }
}