package io.cdap.wrangler.parser.recipe.directives;

import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.DirectiveContext;
import io.cdap.wrangler.api.parser.DirectiveInfo;
import io.cdap.wrangler.api.parser.UsageDefinition;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.Token;
import io.cdap.wrangler.api.parser.TokenGroup;
import io.cdap.wrangler.api.executor.ExecutorContext;
import io.cdap.wrangler.api.parser.Text;
import io.cdap.wrangler.api.parser.LongNumber;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;

import java.util.List;
import java.util.ArrayList;

@DirectiveInfo(
    name = "aggregate-stats",
    description = "Aggregates byte size and time duration values across all rows."
)
public class AggregateStats implements Directive {

    private String sizeSourceCol;
    private String timeSourceCol;
    private String sizeTargetCol;
    private String timeTargetCol;

    private long totalBytes = 0;
    private long totalMillis = 0;
    private int count = 0;

    public AggregateStats() {
    }

    @Override
    public UsageDefinition define() {
        return UsageDefinition.builder("aggregate-stats")
            .withArgs(
                ColumnName.of("sizeSourceCol"),
                ColumnName.of("timeSourceCol"),
                ColumnName.of("sizeTargetCol"),
                ColumnName.of("timeTargetCol")
            )
            .build();
    }

    @Override
    public void initialize(DirectiveContext ctx, TokenGroup args) {
        this.sizeSourceCol = ((ColumnName) args.getValue("sizeSourceCol")).value();
        this.timeSourceCol = ((ColumnName) args.getValue("timeSourceCol")).value();
        this.sizeTargetCol = ((ColumnName) args.getValue("sizeTargetCol")).value();
        this.timeTargetCol = ((ColumnName) args.getValue("timeTargetCol")).value();
    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext ctx) {
        for (Row row : rows) {
            Object sizeObj = row.getValue(sizeSourceCol);
            Object timeObj = row.getValue(timeSourceCol);

            long sizeBytes = parseBytes(sizeObj);
            long durationMillis = parseMillis(timeObj);

            totalBytes += sizeBytes;
            totalMillis += durationMillis;
            count++;
        }

        List<Row> result = new ArrayList<>();
        Row output = new Row();

        // Convert bytes to MB (1024*1024)
        double totalSizeMB = totalBytes / (1024.0 * 1024);
        // Convert ms to seconds
        double totalTimeSec = totalMillis / 1000.0;

        output.add(sizeTargetCol, totalSizeMB);
        output.add(timeTargetCol, totalTimeSec);

        result.add(output);
        return result;
    }

    private long parseBytes(Object obj) {
        if (obj instanceof ByteSize) {
            return ((ByteSize) obj).getBytes();
        } else if (obj instanceof String) {
            return ByteSize.parse((String) obj).getBytes();
        }
        return 0;
    }

    private long parseMillis(Object obj) {
        if (obj instanceof TimeDuration) {
            return ((TimeDuration) obj).getMillis();
        } else if (obj instanceof String) {
            return TimeDuration.parse((String) obj).getMillis();
        }
        return 0;
    }
}
