import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { AnalysisRequest } from '../../../features/analysis/models/analysis-request.model';
import { AnalysisResponse } from '../../../features/analysis/models/analysis-response.model';

@Injectable({
  providedIn: 'root'
})
export class AnalysisService {

  private http = inject(HttpClient);

  private readonly apiUrl = '/api/analyse';

  analyze(request: AnalysisRequest): Observable<AnalysisResponse> {

    return this.http.post<AnalysisResponse>(
      this.apiUrl,
      request
    );

  }

}
