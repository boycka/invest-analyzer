import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface BusinessPlanTransferRequest {
  projectName: string;
  companyName: string;
  sector: string;
  analysisSummary: string;
  investmentAmount: number;
  targetReturn: number;
  riskLevel: string;
}

export interface BusinessPlanTransferResponse {
  redirectUrl: string;
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class BusinessPlanTransferService {
  private readonly http = inject(HttpClient);

  transferToBusinessPlan(
    payload: BusinessPlanTransferRequest
  ): Observable<BusinessPlanTransferResponse> {
    return this.http.post<BusinessPlanTransferResponse>(
      '/api/transfer-to-business-plan',
      payload
    );
  }
}