import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AnalysisPdfService {
  private readonly http = inject(HttpClient);

  download(id: number): void {
    this.http.get(`/api/analyse/${id}/pdf`, { responseType: 'blob' }).subscribe(blob => {
      const url = URL.createObjectURL(blob);
      const anchor = document.createElement('a');
      anchor.href = url;
      anchor.download = `analysis-${id}.pdf`;
      anchor.click();
      URL.revokeObjectURL(url);
    });
  }
}