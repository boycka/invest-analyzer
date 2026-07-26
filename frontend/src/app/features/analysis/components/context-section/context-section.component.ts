import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-context-section',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './context-section.component.html',
  styleUrl: './context-section.component.scss'
})
export class ContextSectionComponent {

  @Input({ required: true })

  form!: FormGroup;

}