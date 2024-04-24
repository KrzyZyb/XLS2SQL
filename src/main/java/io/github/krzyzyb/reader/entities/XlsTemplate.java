package io.github.krzyzyb.reader.entities;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;

public record XlsTemplate(HeaderTemplate header, List<Row> rows) {
}