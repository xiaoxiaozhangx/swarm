package gov.usgs.volcanoes.swarm.rsam;

import gov.usgs.volcanoes.core.configfile.ConfigFile;
import gov.usgs.volcanoes.core.math.BinSize;
import gov.usgs.volcanoes.core.util.StringUtils;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * RSAM view settings.
 * @author Tom Parker
 */

public class RsamViewSettings {
  protected static final String DEFAULTS_FILENAME = "RsamDefaults.config";

  protected enum ViewType {
    VALUES, COUNTS;
  }

  protected int valuesPeriodS;
  protected int countsPeriodS;
  protected boolean detrend;
  protected boolean despike;
  protected int despikePeriod;
  protected boolean runningMedian;
  protected double runningMedianPeriodS;
  protected boolean runningMean;
  protected double runningMeanPeriodS;

  protected int eventThreshold;
  protected double eventRatio;
  protected double eventMaxLengthS;
  protected BinSize binSize;
  protected boolean autoScale;
  protected int scaleMax;
  protected int scaleMin;
  
  protected boolean alarm;
  protected String soundFile;
  
/*  public boolean filterOn;
  public Butterworth filter;
  public boolean zeroPhaseShift;*/

  private int spanLengthS;
  private ViewType viewType;

  protected static RsamViewSettings DEFAULT_RSAM_VIEW_SETTINGS;
  private Set<SettingsListener> listeners;

  static {
    DEFAULT_RSAM_VIEW_SETTINGS = new RsamViewSettings();
    DEFAULT_RSAM_VIEW_SETTINGS.viewType = ViewType.VALUES;
    DEFAULT_RSAM_VIEW_SETTINGS.valuesPeriodS = 600;
    DEFAULT_RSAM_VIEW_SETTINGS.countsPeriodS = 10;
    DEFAULT_RSAM_VIEW_SETTINGS.detrend = false;
    DEFAULT_RSAM_VIEW_SETTINGS.despike = false;
    DEFAULT_RSAM_VIEW_SETTINGS.despikePeriod = 0;
    DEFAULT_RSAM_VIEW_SETTINGS.runningMedian = false;
    DEFAULT_RSAM_VIEW_SETTINGS.runningMedianPeriodS = 300;
    DEFAULT_RSAM_VIEW_SETTINGS.runningMean = false;
    DEFAULT_RSAM_VIEW_SETTINGS.runningMeanPeriodS = 300;
    DEFAULT_RSAM_VIEW_SETTINGS.eventThreshold = 50;
    DEFAULT_RSAM_VIEW_SETTINGS.eventRatio = 1.3;
    DEFAULT_RSAM_VIEW_SETTINGS.eventMaxLengthS = 300;
    DEFAULT_RSAM_VIEW_SETTINGS.binSize = BinSize.HOUR;
    DEFAULT_RSAM_VIEW_SETTINGS.spanLengthS = 60 * 60 * 24 * 7;
    DEFAULT_RSAM_VIEW_SETTINGS.autoScale = true;
    DEFAULT_RSAM_VIEW_SETTINGS.scaleMax = 100;
    DEFAULT_RSAM_VIEW_SETTINGS.scaleMin = 0;
    DEFAULT_RSAM_VIEW_SETTINGS.alarm = false;
    DEFAULT_RSAM_VIEW_SETTINGS.soundFile = "ding.wav";
/*    DEFAULT_RSAM_VIEW_SETTINGS.filter = new Butterworth();
    DEFAULT_RSAM_VIEW_SETTINGS.filterOn = false;
    DEFAULT_RSAM_VIEW_SETTINGS.zeroPhaseShift = true;*/

    List<String> candidateNames = new LinkedList<String>();
    candidateNames.add(DEFAULTS_FILENAME);
    candidateNames.add(System.getProperty("user.home") + File.separatorChar + DEFAULTS_FILENAME);
    String defaultsFile = ConfigFile.findConfig(candidateNames);
    if (defaultsFile == null) {
      defaultsFile = DEFAULTS_FILENAME;
    }

    ConfigFile cf = new ConfigFile(defaultsFile);
    if (cf.wasSuccessfullyRead()) {
      ConfigFile sub = cf.getSubConfig("default");
      DEFAULT_RSAM_VIEW_SETTINGS.set(sub);
    } else {
      DEFAULT_RSAM_VIEW_SETTINGS.save(cf, "default");
      cf.writeToFile(DEFAULTS_FILENAME);
    }
  }

  /**
   * Constructor.
   */
  public RsamViewSettings() {
    if (DEFAULT_RSAM_VIEW_SETTINGS != null) {
      copy(DEFAULT_RSAM_VIEW_SETTINGS);
    }

    listeners = new HashSet<SettingsListener>();
  }

  /**
   * Copy in existing RSAM view settings.
   * @param s settings
   */
  public void copy(RsamViewSettings s) {
    viewType = s.viewType;
    valuesPeriodS = s.valuesPeriodS;
    countsPeriodS = s.countsPeriodS;
    detrend = s.detrend;
    despike = s.despike;
    despikePeriod = s.despikePeriod;
    runningMedian = s.runningMedian;
    runningMedianPeriodS = s.runningMedianPeriodS;
    runningMean = s.runningMean;
    runningMeanPeriodS = s.runningMeanPeriodS;
    eventRatio = s.eventRatio;
    eventThreshold = s.eventThreshold;
    eventMaxLengthS = s.eventMaxLengthS;
    binSize = s.binSize;
    autoScale = s.autoScale;
    scaleMax = s.scaleMax;
    scaleMin = s.scaleMin;
    alarm = s.alarm;
    soundFile = s.soundFile;
/*    filterOn = s.filterOn;
    filter = new Butterworth(s.filter);
    zeroPhaseShift = s.zeroPhaseShift;*/
  }

  /**
   * Set settings from configuration file.
   * @param cf config file
   */
  public void set(ConfigFile cf) {
    viewType = ViewType.valueOf(cf.getString("viewType"));
    valuesPeriodS = StringUtils.stringToInt(cf.getString("valuesPeriod"),
        DEFAULT_RSAM_VIEW_SETTINGS.valuesPeriodS);
    countsPeriodS = StringUtils.stringToInt(cf.getString("countsPeriod"),
        DEFAULT_RSAM_VIEW_SETTINGS.countsPeriodS);
    detrend =
        StringUtils.stringToBoolean(cf.getString("detrend"), DEFAULT_RSAM_VIEW_SETTINGS.detrend);
    despike =
        StringUtils.stringToBoolean(cf.getString("despike"), DEFAULT_RSAM_VIEW_SETTINGS.despike);
    despikePeriod = StringUtils.stringToInt(cf.getString("despikePeriod"),
        DEFAULT_RSAM_VIEW_SETTINGS.despikePeriod);
    runningMedian = StringUtils.stringToBoolean(cf.getString("runningMedian"),
        DEFAULT_RSAM_VIEW_SETTINGS.runningMedian);
    runningMedianPeriodS = StringUtils.stringToDouble(cf.getString("runningMedianPeriod"),
        DEFAULT_RSAM_VIEW_SETTINGS.runningMedianPeriodS);
    runningMean = StringUtils.stringToBoolean(cf.getString("runningMean"),
        DEFAULT_RSAM_VIEW_SETTINGS.runningMean);
    runningMeanPeriodS = StringUtils.stringToDouble(cf.getString("runningMeanPeriod"),
        DEFAULT_RSAM_VIEW_SETTINGS.runningMeanPeriodS);
    eventRatio = StringUtils.stringToDouble(cf.getString("eventRatio"),
        DEFAULT_RSAM_VIEW_SETTINGS.eventRatio);
    eventThreshold = StringUtils.stringToInt(cf.getString("eventThreshold"),
        DEFAULT_RSAM_VIEW_SETTINGS.eventThreshold);
    eventMaxLengthS = StringUtils.stringToDouble(cf.getString("eventMaxLength"),
        DEFAULT_RSAM_VIEW_SETTINGS.eventMaxLengthS);
    binSize = BinSize.fromString(cf.getString("binSize"));
    autoScale = StringUtils.stringToBoolean(cf.getString("autoScale"),
        DEFAULT_RSAM_VIEW_SETTINGS.autoScale);
    scaleMax =
        StringUtils.stringToInt(cf.getString("scaleMax"), DEFAULT_RSAM_VIEW_SETTINGS.scaleMax);
    scaleMin =
        StringUtils.stringToInt(cf.getString("scaleMin"), DEFAULT_RSAM_VIEW_SETTINGS.scaleMin);
    alarm =
        StringUtils.stringToBoolean(cf.getString("alarm"), DEFAULT_RSAM_VIEW_SETTINGS.alarm);
    soundFile =
        StringUtils.stringToString(cf.getString("soundFile"), DEFAULT_RSAM_VIEW_SETTINGS.soundFile);


    //filter.set(cf.getSubConfig("filter"));
    //filterOn =
    //    StringUtils.stringToBoolean(cf.getString("filterOn"), DEFAULT_RSAM_VIEW_SETTINGS.filterOn);
    //zeroPhaseShift = StringUtils.stringToBoolean(cf.getString("zeroPhaseShift"),
    //    DEFAULT_RSAM_VIEW_SETTINGS.zeroPhaseShift);
    
  }

  /**
   * Save settings to configuration file.
   * @param cf config file
   * @param prefix config file prefix
   */
  public void save(ConfigFile cf, String prefix) {
    cf.put(prefix + ".valuesPeriod", Integer.toString(valuesPeriodS));
    cf.put(prefix + ".countsPeriod", Integer.toString(countsPeriodS));
    cf.put(prefix + ".viewType", viewType.toString());
    cf.put(prefix + ".detrend", Boolean.toString(detrend));
    cf.put(prefix + ".despike", Boolean.toString(despike));
    cf.put(prefix + ".despikePeriod", Integer.toString(despikePeriod));
    cf.put(prefix + ".runningMedian", Boolean.toString(runningMedian));
    cf.put(prefix + ".runningMedianPeriod", Double.toString(runningMedianPeriodS));
    cf.put(prefix + ".runningMean", Boolean.toString(runningMean));
    cf.put(prefix + ".runningMeanPeriod", Double.toString(runningMeanPeriodS));
    cf.put(prefix + ".eventRatio", Double.toString(eventRatio));
    cf.put(prefix + ".eventThreshold", Integer.toString(eventThreshold));
    cf.put(prefix + ".eventMaxLength", Double.toString(eventMaxLengthS));
    cf.put(prefix + ".binSize", binSize.toString());
    cf.put(prefix + ".autoScale", Boolean.toString(autoScale));
    cf.put(prefix + ".scaleMax", Double.toString(scaleMax));
    cf.put(prefix + ".scaleMin", Double.toString(scaleMin));
    cf.put(prefix + ".alarm", Boolean.toString(alarm));
    cf.put(prefix + ".soundFile", soundFile);
    //filter.save(cf, prefix + ".filter");
    //cf.put(prefix + ".filterOn", Boolean.toString(filterOn));
    //cf.put(prefix + ".zeroPhaseShift", Boolean.toString(zeroPhaseShift));
  }

  public void setSpanLength(int spanLengthS) {
    this.spanLengthS = spanLengthS;
    notifyListeners();
  }

  public int getSpanLength() {
    return spanLengthS;
  }

  public void setType(ViewType t) {
    viewType = t;
    notifyListeners();
  }

  public ViewType getType() {
    return viewType;
  }

  /**
   * Cycle through view types.
   */
  public void cycleType() {
    switch (viewType) {
      case VALUES:
        viewType = ViewType.COUNTS;
        break;
      case COUNTS:
        viewType = ViewType.VALUES;
        break;
      default:
        break;
    }
    notifyListeners();
  }

  public void addListener(SettingsListener l) {
    listeners.add(l);
  }

  private void notifyListeners() {
    for (SettingsListener l : listeners) {
      l.settingsChanged();
    }
  }
}
