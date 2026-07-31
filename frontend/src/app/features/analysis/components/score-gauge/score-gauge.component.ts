import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-score-gauge',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './score-gauge.component.html',
  styleUrl: './score-gauge.component.scss'
})
export class ScoreGaugeComponent {
  @Input() label = '';
  @Input() score = 0;
  @Input() explanation = '';

  get normalizedScore(): number {
    return Math.max(0, Math.min(100, this.score || 0));
  }

  get gaugeColor(): string {
    if (this.normalizedScore <= 40) return '#dc2626';
    if (this.normalizedScore <= 65) return '#ea8a00';
    return '#16a34a';
  }

  get gaugeBackground(): string {
    return `conic-gradient(${this.gaugeColor} ${this.normalizedScore}%, #e2e8f0 0)`;
  }
}