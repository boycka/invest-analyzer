import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, timeout } from 'rxjs';
import { AnalysisRequest } from '../models/analysis-request.model';
import { AnalysisResponse } from '../models/analysis-response.model';

export interface PaymentCreateResponse { orderId: string; approvalUrl: string; status: string; amount: string; currency: string; }
export interface PaymentConfirmResponse { orderId: string; status: string; analysis: AnalysisResponse; }

@Injectable({ providedIn: 'root' })
export class PaymentService {
  private readonly http = inject(HttpClient);

  createOrder(request: AnalysisRequest): Observable<PaymentCreateResponse> {
    return this.http.post<PaymentCreateResponse>('/api/analyse/paiement', request).pipe(timeout({ each: 15000 }));
  }

  confirm(orderId: string, analysis: AnalysisRequest): Observable<PaymentConfirmResponse> {
    return this.http.post<PaymentConfirmResponse>('/api/analyse/paiement/confirmer', { orderId, analysis }).pipe(timeout({ each: 30000 }));
  }
}