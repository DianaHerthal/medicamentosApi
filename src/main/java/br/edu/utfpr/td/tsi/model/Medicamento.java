package br.edu.utfpr.td.tsi.model;

import com.opencsv.bean.CsvBindByName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "medicamentos")
@Getter
@Setter
public class Medicamento {

    @Id
    private String id;

    @CsvBindByName(column = "NOME_PRODUTO")
    @Field("NOME_PRODUTO")
    @NotBlank(message = "Nome é obrigatório.")
    @Size(min = 3, max = 150)
    private String nomeMedicamento;

    @CsvBindByName(column = "DATA_FINALIZACAO_PROCESSO")
    @Field("DATA_FINALIZACAO_PROCESSO")
    private String dtFinalizacaoProcesso;

    @CsvBindByName(column = "CATEGORIA_REGULATORIA")
    @Field("CATEGORIA_REGULATORIA")
    private String categoriaRegulatoria;

    @CsvBindByName(column = "NUMERO_REGISTRO_PRODUTO")
    @Field("NUMERO_REGISTRO_PRODUTO")
    private String numeroRegistroProduto;

    @CsvBindByName(column = "DATA_VENCIMENTO_REGISTRO")
    @Field("DATA_VENCIMENTO_REGISTRO")
    private String dtVencimentoRegistro;

    @CsvBindByName(column = "NUMERO_PROCESSO")
    @Field("NUMERO_PROCESSO")
    private String numeroProcesso;

    @CsvBindByName(column = "NM_SOCIAL")
    @Field("NM_SOCIAL")
    private String nomeSocial;

    @CsvBindByName(column = "CLASSE_TERAPEUTICA")
    @Field("CLASSE_TERAPEUTICA")
    private String classeTerapeutica;

    @CsvBindByName(column = "EMPRESA_DETENTORA_REGISTRO")
    @Field("EMPRESA_DETENTORA_REGISTRO")
    private String empresaDetentoraRegistro;    

    @CsvBindByName(column = "SITUACAO_REGISTRO")
    @Field("SITUACAO_REGISTRO")
    private String situacaoRegistro; // Alterado de Enum<SituacaoRegistro> para String para aceitar "VÁLIDO" e "CADUCO/CANCELADO"

    @CsvBindByName(column = "PRINCIPIO_ATIVO")
    @Field("PRINCIPIO_ATIVO")
    private String principioAtivo;

}
