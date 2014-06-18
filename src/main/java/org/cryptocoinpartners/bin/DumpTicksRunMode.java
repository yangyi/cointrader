package org.cryptocoinpartners.bin;


import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.clutch.dates.StringToTime;
import org.cryptocoinpartners.module.Context;
import org.cryptocoinpartners.module.SaveTicksCsv;
import org.cryptocoinpartners.module.TickWindow;
import org.cryptocoinpartners.util.Config;
import org.cryptocoinpartners.util.Replay;
import org.joda.time.Instant;

import java.util.Date;
import java.util.List;


@SuppressWarnings("UnusedDeclaration")
@Parameters( commandNames = "dump-ticks", commandDescription = "generate ticks into a csv file" )
public class DumpTicksRunMode extends RunMode
{

    public void run()
    {
        // parse the start and end times
        Date start = null;
        if( startStr != null ) {
            try {
                start = new StringToTime(startStr);
            }
            catch( Exception e ) {
                log.error("Could not parse start time \"" + startStr + "\"");
                System.exit(7001);
            }
        }

        Date end = null;
        if( endStr != null ) {
            try {
                end = new StringToTime(endStr);
            }
            catch( Exception e ) {
                log.error("Could not parse end time \"" + endStr + "\"");
                System.exit(7001);
            }
        }


        Replay replay;
        if( start == null ) {
            if( end == null )
                replay = Replay.all(false);
            else
                replay = Replay.until(new Instant(end),false);
        }
        else if( end == null )
            replay = Replay.since(new Instant(start),false);
        else
            replay = Replay.between(new Instant(start), new Instant(end),false);


        Context context = replay.getContext();
        context.attach(TickWindow.class); // generate ticks
        context.attach(SaveTicksCsv.class,  // save ticks as csv
                       Config.module( "savetickscsv.filename", filenames.get(0),
                                      "savetickscsv.na", allowNa )
                      );
        replay.run();
        context.destroy();
        System.exit(0);
    }


    @Parameter( names = { "-start" }, description = "English time description of the time to start dumping ticks")
    public String startStr = null;


    @Parameter( names = { "-end" }, description = "English time description of the time to stop dumping ticks" )
    public String endStr = null;


    @Parameter( names = "-na", description = "If set, any ticks which are missing data (no Book or last Trade) will still be output")
    public boolean allowNa = false;


    @Parameter( required = true, arity = 1, description = "output filename")
    public List<String> filenames;
}
