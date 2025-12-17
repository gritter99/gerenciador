import { Component, OnInit, signal } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { TransacaoService } from '../../core/services/transacao.service';
import { Transacao, TipoTransacao } from '../../models';
import { TransacaoFormComponent } from '../transacao-form/transacao-form.component';

@Component({
  selector: 'app-transacoes',
  standalone: true,
  imports: [CommonModule, CurrencyPipe, DatePipe, TransacaoFormComponent],
  templateUrl: './transacoes.component.html'
})
export class TransacoesComponent implements OnInit {
  transacoes = signal<Transacao[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);

  showForm = signal(false);
  tipoPreSelecionado = signal<TipoTransacao | null>(null);

  constructor(
    private transacaoService: TransacaoService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.loadTransacoes();

    // Verifica query params para abrir form automaticamente
    this.route.queryParams.subscribe(params => {
      if (params['action'] === 'nova') {
        this.tipoPreSelecionado.set(params['tipo'] as TipoTransacao || null);
        this.showForm.set(true);
      }
    });
  }

  loadTransacoes(): void {
    this.loading.set(true);
    this.error.set(null);

    this.transacaoService.listar().subscribe({
      next: (data) => {
        this.transacoes.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Erro ao carregar transações');
        this.loading.set(false);
        console.error('Transacoes error:', err);
      }
    });
  }

  excluir(transacao: Transacao): void {
    if (!confirm(`Deseja excluir a transação "${transacao.descricao}"?`)) {
      return;
    }

    this.transacaoService.excluir(transacao.id).subscribe({
      next: () => {
        this.transacoes.update(list => list.filter(t => t.id !== transacao.id));
      },
      error: (err) => {
        alert('Erro ao excluir transação');
        console.error('Delete error:', err);
      }
    });
  }

  abrirForm(tipo?: TipoTransacao): void {
    this.tipoPreSelecionado.set(tipo || null);
    this.showForm.set(true);
  }

  fecharForm(): void {
    this.showForm.set(false);
    this.tipoPreSelecionado.set(null);
  }

  onTransacaoCriada(transacao: Transacao): void {
    this.transacoes.update(list => [transacao, ...list]);
    this.fecharForm();
  }
}

