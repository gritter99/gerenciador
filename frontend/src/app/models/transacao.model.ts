export type TipoTransacao = 'RECEITA' | 'DESPESA';

export interface Transacao {
  id: string;
  tipo: TipoTransacao;
  data: string; // LocalDate como string ISO (YYYY-MM-DD)
  valor: number;
  descricao: string;
}

export interface TransacaoRequest {
  tipo: TipoTransacao;
  data: string;
  valor: number;
  descricao: string;
}

