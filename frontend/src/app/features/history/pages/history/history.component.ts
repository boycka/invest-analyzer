import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
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

    voirDetail(history: History): void {
    this.historyService.getById(history.id).subscribe(result => {
        this.router.navigate(['/analysis-result'], {
        state: { result }
        });
    });
    }

  relancerAnalyse(history: History): void {
    this.historyService.getById(history.id).subscribe(record => {
      this.router.navigate(['/'], {
        state: { prefill: record.request }
      });
    });
  }

}