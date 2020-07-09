package br.com.arrobatecinformatica.agendacontatos.database;

/**
 * Created by Alexandre on 15/04/2017.
 */

public class ScriptSQL {

    public static String CreateContato() {

        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append(" CREATE TABLE IF NOT EXISTS CONTATO ( ");
        sqlBuilder.append("_id                INTEGER   NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sqlBuilder.append("NOME               VARCHAR (200), ");
        sqlBuilder.append("TELEFONE           VARCHAR (14), ");
        sqlBuilder.append("TIPOTELEFONE       VARCHAR (1), ");
        sqlBuilder.append("EMAIL              VARCHAR (255), ");
        sqlBuilder.append("TIPOEMAIL          VARCHAR (1), ");
        sqlBuilder.append("ENDERECO           VARCHAR (255), ");
        sqlBuilder.append("TIPOENDERECO       VARCHAR (1), ");
        sqlBuilder.append("DATASESPECIAIS     DATE, ");
        sqlBuilder.append("TIPODATASESPECIAIS VARCHAR (1), ");
        sqlBuilder.append("GRUPOS             VARCHAR (255) ");
        sqlBuilder.append(");");

        return sqlBuilder.toString();

    }

    public static String DropContato() {

        StringBuilder sql = new StringBuilder();

        sql.append(" DROP TABLE IF EXISTS CONTATO ");
        return sql.toString();
    }
}
