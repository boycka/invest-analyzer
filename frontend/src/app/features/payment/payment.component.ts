import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AnalysisRequest } from '../analysis/models/analysis-request.model';
import { AnalysisService } from '../analysis/services/analysis.service';
import { PaymentCreateResponse, PaymentService } from '../analysis/services/payment.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.scss'
})
export class PaymentComponent {
  private readonly router = inject(Router);
  private readonly paymentService = inject(PaymentService);
  private readonly analysisService = inject(AnalysisService);

  request!: AnalysisRequest;
  order: PaymentCreateResponse | null = null;
  isLoading = true;
  isConfirming = false;
  errorMessage = '';
  readonly allowPaymentSkip = environment.allowPaymentSkip;

  constructor() {
    const state = this.router.getCurrentNavigation()?.extras.state;
    this.request = state?.['request'];
    if (!this.request) {
      this.errorMessage = 'Les donnees du projet sont manquantes. Recommencez le formulaire.';
      this.isLoading = false;
      return;
    }
    this.paymentService.createOrder(this.request).subscribe({
      next: order => { this.order = order; this.isLoading = false; },
      error: error => { this.errorMessage = error?.error?.error || 'PayPal Sandbox est indisponible.'; this.isLoading = false; }
    });
  }

  confirmPayment(): void {
    if (!this.order || this.isConfirming) return;
    this.isConfirming = true;
    this.errorMessage = '';
    this.paymentService.confirm(this.order.orderId, this.request).subscribe({
      next: response => this.router.navigate(['/analysis-result'], { state: { result: response.analysis, request: this.request } }),
      error: error => { this.isConfirming = false; this.errorMessage = error?.error?.error || "Le paiement n'est pas encore confirme."; }
    });
  }

  continueWithoutPayment(): void {
    if (!this.allowPaymentSkip || !this.request || this.isConfirming) return;
    this.isConfirming = true;
    this.errorMessage = '';
    this.analysisService.analyze(this.request).subscribe({
      next: result => this.router.navigate(['/analysis-result'], { state: { result, request: this.request } }),
      error: error => { this.isConfirming = false; this.errorMessage = error?.error?.error || 'Analyse indisponible pour le moment.'; }
    });
  }

  cancel(): void { this.router.navigate(['/']); }
}