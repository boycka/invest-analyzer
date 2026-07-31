import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { AnalysisDetailResponse } from '../../analysis/models/analysis-response.model';
import { History } from '../models/history.model';

@Injectable({ providedIn: 'root' })
export class HistoryService {
  private readonly http = inject(HttpClient);

  getHistory(): Observable<History[]> {
    return this.http.get<History[]>('/api/analyse/historique');
  }

  getById(id: number): Observable<AnalysisDetailResponse> {
    return this.http.get<AnalysisDetailResponse>(`/api/analyse/${id}/resultat`);
  }
}