import { Component, OnInit, inject } from '@angular/core';

import { CommonModule } from '@angular/common';

import { HistoryService } from '../../services/history.service';

import { History } from '../../models/history.model';

@Component({

  selector: 'app-history',

  standalone: true,

  imports: [CommonModule],

  templateUrl: './history.component.html',

  styleUrl: './history.component.scss'

})

export class HistoryComponent implements OnInit {

  private readonly historyService = inject(HistoryService);

  histories: History[] = [];

  loading = true;

  ngOnInit(): void {

    this.historyService.getHistory().subscribe({

      next: data => {

        this.histories = data;

        this.loading = false;

      },

      error: err => {

        console.error(err);

        this.loading = false;

      }

    });

  }

}