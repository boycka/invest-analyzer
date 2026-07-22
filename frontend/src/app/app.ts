import { Component, inject, OnInit } from '@angular/core';

import {
  HealthResponse,
  HealthService
} from './core/services/health';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  private readonly healthService = inject(HealthService);

  backendStatus = 'Checking backend...';
  errorMessage = '';

  ngOnInit(): void {
    this.healthService.check().subscribe({
      next: (response: HealthResponse) => {
        this.backendStatus =
          `${response.service}: ${response.status}`;
      },
      error: (error: unknown) => {
        console.error('Backend health check failed', error);
        this.backendStatus = 'Backend unavailable';
        this.errorMessage =
          'Angular could not connect to the Spring Boot API.';
      }
    });
  }
}