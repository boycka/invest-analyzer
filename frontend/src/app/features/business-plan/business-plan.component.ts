import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

interface BusinessPlanPayload {
  projectName: string;
  companyName: string;
  sector: string;
  analysisSummary: string;
  investmentAmount: number;
  targetReturn: number;
  riskLevel: string;
  issuedAt: number;
  expiresAt: number;
}

@Component({
  selector: 'app-business-plan',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './business-plan.component.html',
  styleUrl: './business-plan.component.scss'
})
export class BusinessPlanComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);

  payload: BusinessPlanPayload | null = null;
  errorMessage = '';

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');

    if (!token) {
      this.errorMessage = 'Missing transfer token. Return to the analysis form and generate it again.';
      return;
    }

    try {
      this.payload = this.decodePayload(token);
    } catch {
      this.errorMessage = 'The transfer token could not be decoded.';
    }
  }

  private decodePayload(token: string): BusinessPlanPayload {
    const parts = token.split('.');

    if (parts.length !== 3) {
      throw new Error('Invalid JWT token');
    }

    const payloadJson = this.base64UrlDecode(parts[1]);
    return JSON.parse(payloadJson) as BusinessPlanPayload;
  }

  private base64UrlDecode(value: string): string {
    const normalized = value.replace(/-/g, '+').replace(/_/g, '/');
    const padded = normalized + '='.repeat((4 - (normalized.length % 4)) % 4);
    return decodeURIComponent(
      Array.from(atob(padded), (character) => `%${character.charCodeAt(0).toString(16).padStart(2, '0')}`).join('')
    );
  }
}