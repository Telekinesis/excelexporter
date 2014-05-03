package excelexporter.ganglia.lineparser;

import java.util.List;

import org.telekinesis.commonclasses.entity.Pair;
import org.telekinesis.commonclasses.io.LineParser;

public interface DiskLineParser extends LineParser
{
    public List<Pair<Long, Double>> getResult();
}
