package ru.esstu.news.aggregator.utils;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class ProgressPrinter {
    // частота обновления (мс)
    private static final long REFRESH_INTERVAL = 1000;
    private final  List<String> labels;
    private final List<List<CountDownLatch>> latches;
    private final List<List<Integer>> totalCounts;

    public ProgressPrinter(
            List<String> labels,
            List<List<CountDownLatch>> latches,
            List<List<Integer>> totalCounts
    ) {
        this.labels = labels.stream().map(label -> ">>> " + label).toList();
        this.latches = latches;
        this.totalCounts = totalCounts;
    }

    public void printMultiProgress() throws InterruptedException {
        int groups = latches.size();
        int printedRows = 0;
        // 1) Выводим начальные пустые бары
        for (int i = 0; i < groups; i++) {
            var groupCounts = totalCounts.get(i);
            System.out.println(labels.get(i));
            printedRows++;
            int j = 0;
            for (Integer totalCount : groupCounts) {
                System.out.println(renderLine((++j) +".", 0, totalCount));
                printedRows++;
            }
        }

        // 2) Пока есть не-нулевые latch’и, дергаем обновление
        boolean doneAll;
        do {
            Thread.sleep(REFRESH_INTERVAL);

            // перемещаем курсор вверх на printedRows строк
            System.out.print("\u001B[" + printedRows + "A");

            doneAll = true;
            for (int i = 0; i < groups; i++) {
                var groupCounts = totalCounts.get(i);
                var groupLatches = latches.get(i);
                System.out.println(labels.get(i));
                for (int j = 0; j < groupLatches.size(); j++) {
                    var totalCount = groupCounts.get(j);
                    long remaining = groupLatches.get(j).getCount();
                    int done = totalCount - (int) remaining;
                    if (remaining > 0) doneAll = false;

                    // очистить текущую строку
                    System.out.print("\u001B[2K");
                    // и вывести заново
                    System.out.println(renderLine((j) +".", done, totalCount));
                }
            }
        } while (!doneAll);
    }

    /** Рендер одной строки: [=====     ] 5/10 */
    private static String renderLine(String label, int done, int total) {
        int width = 30; // символов внутри []
        int filled = (int) (1.0 * done / total * width);
        String bar = "=".repeat(Math.max(0, filled)) +
                " ".repeat(Math.max(0, width - filled));
        return String.format("%-10s [%s] %4d/%4d", label, bar, done, total);
    }
}
