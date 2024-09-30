package com.redgogh.tools.poi;

/* -------------------------------------------------------------------------------- *\
|*                                                                                  *|
|*    Copyright (C) 2019-2024 RedGogh All rights reserved.                          *|
|*                                                                                  *|
|*    Licensed under the Apache License, Version 2.0 (the "License");               *|
|*    you may not use this file except in compliance with the License.              *|
|*    You may obtain a copy of the License at                                       *|
|*                                                                                  *|
|*        http://www.apache.org/licenses/LICENSE-2.0                                *|
|*                                                                                  *|
|*    Unless required by applicable law or agreed to in writing, software           *|
|*    distributed under the License is distributed on an "AS IS" BASIS,             *|
|*    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.      *|
|*    See the License for the specific language governing permissions and           *|
|*    limitations under the License.                                                *|
|*                                                                                  *|
\* -------------------------------------------------------------------------------- */

import com.redgogh.tools.BasicConverter;
import com.redgogh.tools.Optional;
import com.redgogh.tools.collection.Lists;
import com.redgogh.tools.io.File;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.io.OutputStream;

import static com.redgogh.tools.Assert.throwIfError;
import static com.redgogh.tools.BasicConverter.atos;
import static com.redgogh.tools.StringUtils.strnempty;

/**
 * 类 {@link Workbook} 用于创建和操作 Excel 工作簿。
 *
 * <p>该类使用 Apache POI 库来实现工作簿的创建和行的添加。
 * 默认情况下，会创建一个名为 "Sheet1" 的工作表。
 *
 * <p>可以通过 {@link #addRow(Object...)} 方法添加行数据，
 * 通过 {@link #getRow(int)} 方法获取指定行的数据。
 *
 * <p>调用 {@link #write(OutputStream)} 方法可以将工作簿写入指定的输出流。
 *
 * <p>注意：在使用此类时，请确保已添加 Apache POI 依赖。
 *
 * @author RedGogh
 */
public class Workbook {

    /**
     * Apache POI 工作簿实例，用于创建和操作 Excel 文件。
     */
    private final org.apache.poi.ss.usermodel.Workbook wb;

    /**
     * 当前工作簿中的工作表。
     */
    private Sheet sheet;

    /**
     * #brief: 创建一个空的 Workbook 实例
     *
     * <p>使用默认的 XSSFWorkbook 创建一个新的 Workbook
     * 对象。
     */
    public Workbook() {
        this(new XSSFWorkbook());
    }

    /**
     * #brief: 根据指定路径创建 Workbook 实例
     *
     * <p>根据提供的文件路径，创建一个新的 Workbook
     * 实例。如果文件不存在或格式不正确，可能会抛出
     * 异常。
     *
     * @param pathname Excel 文件的路径
     */
    public Workbook(String pathname) {
        this(new File(pathname));
    }

    /**
     * #brief: 根据指定文件创建 Workbook 实例
     *
     * <p>根据提供的 File 对象，创建一个新的 Workbook
     * 实例。如果文件不存在或格式不正确，可能会抛出
     * 异常。
     *
     * @param file Excel 文件对象
     */
    public Workbook(File file) {
        this(throwIfError(() -> new XSSFWorkbook(file)));
    }

    /**
     * #brief: 根据输入流创建 Workbook 实例
     *
     * <p>根据提供的输入流，创建一个新的 Workbook
     * 实例。输入流需指向有效的 Excel 文件内容。
     * 如果输入流无效，可能会抛出异常。
     *
     * @param stream 输入流，需指向有效的 Excel 文件内容
     */
    public Workbook(InputStream stream) {
        this(throwIfError(() -> new XSSFWorkbook(stream)));
    }

    /**
     * #brief: 使用现有的 XSSFWorkbook 创建 Workbook 实例
     *
     * <p>接受一个已存在的 XSSFWorkbook 对象并将其
     * 包装为新的 Workbook 实例。
     *
     * @param workbook 已存在的 XSSFWorkbook 对象
     */
    public Workbook(XSSFWorkbook workbook) {
        this.wb = workbook;
        /* switch sheet. */
        this.sheet = wb.getSheet(wb.getSheetName(0));
    }


    /**
     * 检查并获取指定名称的工作表。如果不存在，则创建一个新的工作表。
     *
     * <p>此方法用于确保在操作之前工作表存在，如果指定名称的工作表不存在，将自动创建。
     *
     * <p>示例用法：
     * <pre>
     *      workbook.checkout("Sheet2");
     * </pre>
     *
     * @param name 工作表的名称。
     * @throws IllegalArgumentException 如果工作表名称为 {@code null} 或空字符串。
     */
    public void checkout(String name) {
        sheet = Optional.ifNull(wb.getSheet(name), wb.createSheet(name));
    }

    /**
     * 向工作表添加一行数据。
     *
     * <p>此方法将接受任意数量的参数，并将它们作为单元格的内容添加到当前工作表中。
     *
     * <p>注意：如果参数为 null，单元格将设置为一个空字符串。
     *
     * <p>示例用法：
     * <pre>
     *      Workbook workbook = new Workbook();
     *      workbook.addRow("数据1", "数据2", "数据3");
     * </pre>
     *
     * @param values 行数据，类型为 Object 变长参数。
     *
     */
    public void addRow(Object... values) {
        addRow(new Row(Lists.map(values, BasicConverter::atos)));
    }

    /**
     * 向工作表添加一行 {@link Row} 对象。
     *
     * <p>此方法将使用给定的 {@link Row} 对象将数据添加到当前工作表。
     *
     * <p>注意：确保 {@code rowUnit} 不是 null。
     *
     * <p>示例用法：
     * <pre>
     *      Row row = new Row();
     *      row.add("数据1");
     *      workbook.addRow(row);
     * </pre>
     *
     * @param rowUnit 行数据的 {@link Row} 对象。
     * @throws NullPointerException 如果 {@code rowUnit} 为 null。
     *
     */
    public void addRow(Row rowUnit) {
        int rowNum = sheet.getLastRowNum();
        org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum + 1);
        for (int i = 0; i < rowUnit.size(); i++) {
            row.createCell(i).setCellValue(atos(rowUnit.get(i)));
        }
    }

    /**
     * 获取指定索引的行数据。
     *
     * <p>此方法返回指定索引的行的数据，作为一个 {@link Row} 对象。
     *
     * <p>注意：索引必须在有效范围内，否则会抛出异常。
     *
     * <p>示例用法：
     * <pre>
     *      Row row = workbook.getRow(0);
     * </pre>
     *
     * @param index 行的索引。
     * @return {@link Row} 对象，包含该行的所有单元格数据。
     * @throws IndexOutOfBoundsException 如果索引超出范围。
     *
     */
    public Row getRow(int index) {
        org.apache.poi.ss.usermodel.Row row = sheet.getRow(index);
        Row retval = new Row();

        short lastCellNum = row.getLastCellNum();
        for (short i = 0; i < lastCellNum; i++) {
            retval.add(row.getCell(i).getStringCellValue());
        }

        return retval;
    }

    /**
     * #brief: 获取当前工作表的行数
     *
     * <p>返回当前工作表中最后一行的索引，代表
     * 行数。注意：行数从 0 开始，因此返回值加 1
     * 才是实际行数。如果工作表为空，返回 -1。
     *
     * @return 当前工作表的行数
     */
    public int rowCount() {
        return sheet.getLastRowNum();
    }

    /**
     * #brief: 根据文件路径加载 Workbook 实例
     *
     * <p>根据提供的文件路径，加载并返回对应的
     * Workbook 实例。如果文件不存在或格式不正确，可能
     * 会抛出异常。
     *
     * @param pathname Excel 文件的路径
     * @return 加载的 Workbook 实例
     */
    public static Workbook load(String pathname) {
        return load(new File(pathname));
    }

    /**
     * #brief: 根据 File 对象加载 Workbook 实例
     *
     * <p>根据提供的 File 对象，加载并返回对应的
     * Workbook 实例。如果文件不存在或格式不正确，可能
     * 会抛出异常。
     *
     * @param file Excel 文件对象
     * @return 加载的 Workbook 实例
     */
    public static Workbook load(File file) {
        return new Workbook(file);
    }

    /**
     * #brief: 根据输入流加载 Workbook 实例
     *
     * <p>根据提供的输入流，加载并返回对应的
     * Workbook 实例。输入流需指向有效的 Excel 文件内容。
     * 如果输入流无效，可能会抛出异常。
     *
     * @param stream 输入流，需指向有效的 Excel 文件内容
     * @return 加载的 Workbook 实例
     */
    public static Workbook load(InputStream stream) {
        return new Workbook(stream);
    }

    /**
     * 将工作簿写入指定的输出流。
     *
     * <p>使用此方法可以将当前工作簿的数据输出到文件或其他输出流中。
     *
     * <p>注意：在写入流之前，请确保流已经打开并可以写入。
     *
     * <p>示例用法：
     * <pre>
     *      try (OutputStream stream = new FileOutputStream("output.xlsx")) {
     *          workbook.write(stream);
     *      }
     * </pre>
     *
     * @param stream 输出流，用于写入工作簿数据。
     *
     */
    public void write(OutputStream stream) {
        throwIfError(() -> wb.write(stream));
    }

}
