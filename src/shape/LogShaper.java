package shape;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * ログファイルを読み込み、CSVファイルとして出力する。
 *
 */
public class LogShaper {

	private static final String UTF_8 = "UTF-8";
	private static final String USER_DIR = "user.dir";
	/* 生成するCSVファイルのヘッダー文字列 */
	private static final String CSV_HEADER = "timestamp,messageType,path,message\n";

	public static void main(String[] args) {
		// ファイルパスを実行時の引数として受け取る
		String filepath = args[0];
		List<String> lines = readLog(filepath);
		List<String> newLines = shapeLogIntoCsv(lines);
		writeCsv(newLines);
		System.out.println("処理が終了しました。");
	}

	/**
	 * ログデータを読み込み、行データのリストを返す。
	 *
	 * @param filepath 読み込むファイルのパス
	 * @return 読み込んだファイルの行ごとデータ
	 */
	private static List<String> readLog(String filepath) {
		BufferedReader br = null;
		List<String> lines = new ArrayList<String>();
		try {
			File file = new File(filepath);
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), UTF_8));
			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			System.out.println(e);
			System.out.println("ファイル(" + filepath + ")を開けませんでした。ファイルを確認してください。");
		} finally {
			try {
				br.close();
			} catch (IOException e2) {
				System.out.println(e2);
				System.out.println("ファイルのクローズに失敗しました。");
			}
		}
		return lines;
	}

	/**
	 * 行データを整形し、カンマ区切りにして返す。
	 *
	 * @param lines 行データ
	 * @return newLines カンマ区切りの行データ
	 */
	private static List<String> shapeLogIntoCsv(List<String> lines) {
		List<String> newLines = new ArrayList<String>();
		int rowNum = 0;
		int columnNum = 4;
		for (String line: lines) {
			rowNum++;
			// まず半角スペース二つ以上の区切りをカンマで区切る
			line = line.replaceAll("  +", ",");
			// 次に半角スペースと角かっこの区切りをカンマで区切る
			String notRegex = Pattern.quote(" [");
			line = line.replaceFirst(notRegex, ",\\[");
			// タイムスタンプのカンマは取り除く
			line = line.replaceFirst(",", " ");
			if (line.split(",").length != columnNum) {
				System.out.println(rowNum + "行目付近に適切にカンマ区切りができないデータがあります。");
			}
			newLines.add(line);
		}
		return newLines;
	}

	/**
	 * カンマ区切りの行データをCSVファイルに出力する。
	 *
	 * @param lines カンマ区切りの行データ
	 */
	private static void writeCsv(List<String> lines) {
		BufferedWriter bw = null;
		try {
			String filepath = System.getProperty(USER_DIR);
			File file = new File(filepath + "/log.csv");
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), UTF_8));
			bw.write(CSV_HEADER);
			for (String line: lines) {
				bw.write(line + "\n");
			}
		} catch (IOException e) {
			System.out.println(e);
			System.out.println("ファイルを出力できませんでした。");
		} finally {
			try {
				bw.close();
			} catch (IOException e2) {
				System.out.println(e2);
				System.out.println("ファイルをクローズできませんでした。");
			}
		}
	}

}
