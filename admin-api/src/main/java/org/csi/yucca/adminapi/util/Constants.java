package org.csi.yucca.adminapi.util;

import java.util.Arrays;
import java.util.List;

import org.csi.yucca.adminapi.model.Component;

public class Constants {

	public static final String SCHEMA_DB = "int_yucca.";

//	public static final String SCHEMA_DB = "";
	
	public static final String CLIENT_FORMAT_DATE = "yyyyMMdd";
	
	public static final int PASSWORD_LENGTH = 12;

	public static int INSTALLATION_TENANT_MAX_DATASET_NUM = 20;

	public static int INSTALLATION_TENANT_MAX_STREAMS_NUM = 20;
	
	public static String TWEET_ALIAS_COMPONENT = "-";
	
	public static Integer OTHER_MEASURE_UNIT_ID = 0;
	
	public static List<Component> TWEET_COMPONENTS = Arrays.asList(
			new Component().name("contributors").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(1),	
			new Component().name("createdAt").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.DATE_TIME.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(2),	
			new Component().name("currentUserRetweetId").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.LONG.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(3),	
			new Component().name("favoriteCount").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.INT.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(4),	
			new Component().name("lon").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(5),	
			new Component().name("lat").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(6),
			new Component().name("tweetid").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.LONG.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(7),
			new Component().name("lang").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(8),
			new Component().name("placeName").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(9),	
			new Component().name("retweetCount").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.INT.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(10),
			new Component().name("source").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(11),	
			new Component().name("getText").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(12),
			new Component().name("favorited").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.BOOLEAN.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(13),
			new Component().name("possiblySensitive").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.BOOLEAN.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(14),
			new Component().name("retweet").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.BOOLEAN.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(15),
			new Component().name("retweetedByMe").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(16),	
			new Component().name("truncated").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.BOOLEAN.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(17),
			new Component().name("hashTags").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(18),
			new Component().name("url").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(19),
			new Component().name("media").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(20),	
			new Component().name("mediaUrl").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(21),
			new Component().name("mediaCnt").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(22),	
			new Component().name("userId").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(23),	
			new Component().name("userName").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(24),
			new Component().name("userScreenName").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(25),	
			new Component().name("userMentions").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.STRING.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(26),	
			new Component().name("retweetParentId").alias(TWEET_ALIAS_COMPONENT).idDataType(DataType.LONG.id()).iskey(0).idMeasureUnit(OTHER_MEASURE_UNIT_ID).inorder(27));
	
}
