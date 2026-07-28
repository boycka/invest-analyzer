import { inject, Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { History } from '../models/history.model';

@Injectable({
  providedIn: 'root'
})
export class HistoryService {

  private readonly http = inject(HttpClient);

  getHistory(): Observable<History[]> {

    return this.http.get<History[]>(

      '/api/analyses/history'

    );

  }

}