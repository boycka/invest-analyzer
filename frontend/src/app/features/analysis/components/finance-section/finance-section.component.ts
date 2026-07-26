import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-finance-section',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './finance-section.component.html',
  styleUrl: './finance-section.component.scss'
})
export class FinanceSectionComponent {

  @Input({ required: true })
  form!: FormGroup;

}