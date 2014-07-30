package com.pivotal.pandora.common;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

public class PandoraConfiguration extends YarnConfiguration{

  private static final String PANDORA_DEFAULT_XML_FILE = "pandora-default.xml";
  private static final String PANDORA_SITE_XML_FILE = "pandora-site.xml";

  static {
    Configuration.addDefaultResource(PANDORA_DEFAULT_XML_FILE);
    Configuration.addDefaultResource(PANDORA_SITE_XML_FILE);
  }
  
  public PandoraConfiguration(){
    super();
  }
}
