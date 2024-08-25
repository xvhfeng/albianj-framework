package com.geek94.config;

import java.util.Map;

public interface IConfigParserBefore {
    void initBefore(String filename,Map<?,?> map);
}
