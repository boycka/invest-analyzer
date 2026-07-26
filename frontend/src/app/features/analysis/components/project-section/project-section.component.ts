import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-project-section',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './project-section.component.html',
  styleUrl: './project-section.component.scss'
  
})
export class ProjectSectionComponent {

  @Input({ required: true })
  form!: FormGroup;

}
