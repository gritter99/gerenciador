import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Transacao, TransacaoRequest } from '../../models';

@Injectable({
  providedIn: 'root'
})
export class TransacaoService {
  private readonly apiUrl = `${environment.apiUrl}/transacoes`;

  constructor(private http: HttpClient) { }

  listar(): Observable<Transacao[]> {
    return this.http.get<Transacao[]>(this.apiUrl);
  }

  criar(transacao: TransacaoRequest): Observable<Transacao> {
    return this.http.post<Transacao>(this.apiUrl, transacao);
  }

  excluir(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

