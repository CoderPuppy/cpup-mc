package cpup.mc.lib.logging

import org.apache.logging.log4j
import org.slf4j
import org.slf4j.helpers.MessageFormatter

case class JCLForSLF4JLogger(logger: log4j.Logger) extends slf4j.Logger {
	protected def convertMarker(marker: slf4j.Marker) = log4j.MarkerManager.getMarker(marker.getName)

	override def error(marker: slf4j.Marker, msg: String, t: Throwable) = if(isErrorEnabled(marker)) {
		logger.error(convertMarker(marker), msg, t)
	} else {}

	override def error(marker: slf4j.Marker, format: String, arguments: AnyRef*) = if(isErrorEnabled(marker)) {
		logger.error(convertMarker(marker), MessageFormatter.arrayFormat(format, arguments.toArray).getMessage)
	} else {}

	override def error(marker: slf4j.Marker, format: String, arg1: scala.Any, arg2: scala.Any) = if(isErrorEnabled(marker)) {
		logger.error(convertMarker(marker), MessageFormatter.format(format, arg1, arg2).getMessage)
	} else {}

	override def error(marker: slf4j.Marker, format: String, arg: scala.Any) = if(isErrorEnabled(marker)) {
		logger.error(convertMarker(marker), MessageFormatter.format(format, arg).getMessage)
	} else {}

	override def error(marker: slf4j.Marker, msg: String) = if(isErrorEnabled(marker)) {
		logger.error(convertMarker(marker), msg)
	} else {}

	override def isErrorEnabled(marker: slf4j.Marker) = logger.isErrorEnabled(convertMarker(marker))

	override def error(msg: String, t: Throwable) = if(isErrorEnabled) {
		logger.error(msg, t)
	} else {}

	override def error(format: String, arguments: AnyRef*) = if(isErrorEnabled) {
		logger.error(MessageFormatter.arrayFormat(format, arguments.toArray).getMessage)
	} else {}

	override def error(format: String, arg1: scala.Any, arg2: scala.Any) = if(isErrorEnabled) {
		logger.error(MessageFormatter.format(format, arg1, arg2).getMessage)
	} else {}

	override def error(format: String, arg: scala.Any) = if(isErrorEnabled) {
		logger.error(MessageFormatter.format(format, arg).getMessage)
	} else {}

	override def error(msg: String) = if(isErrorEnabled) {
		logger.error(msg)
	} else {}

	override def isErrorEnabled = logger.isErrorEnabled

	override def warn(marker: slf4j.Marker, msg: String, t: Throwable) = if(isWarnEnabled(marker)) {
		logger.warn(convertMarker(marker), msg, t)
	} else {}

	override def warn(marker: slf4j.Marker, format: String, arguments: AnyRef*) = if(isWarnEnabled(marker)) {
		logger.warn(convertMarker(marker), MessageFormatter.arrayFormat(format, arguments.toArray).getMessage)
	} else {}

	override def warn(marker: slf4j.Marker, format: String, arg1: scala.Any, arg2: scala.Any) = if(isWarnEnabled(marker)) {
		logger.warn(convertMarker(marker), MessageFormatter.format(format, arg1, arg2).getMessage)
	} else {}

	override def warn(marker: slf4j.Marker, format: String, arg: scala.Any) = if(isWarnEnabled(marker)) {
		logger.warn(convertMarker(marker), MessageFormatter.format(format, arg).getMessage)
	} else {}

	override def warn(marker: slf4j.Marker, msg: String) = if(isWarnEnabled(marker)) {
		logger.warn(convertMarker(marker), msg)
	} else {}

	override def isWarnEnabled(marker: slf4j.Marker) = logger.isWarnEnabled(convertMarker(marker))

	override def warn(msg: String, t: Throwable) = if(isWarnEnabled) {
		logger.warn(msg, t)
	} else {}

	override def warn(format: String, arg1: scala.Any, arg2: scala.Any) = if(isWarnEnabled) {
		logger.warn(MessageFormatter.format(format, arg1, arg2).getMessage)
	} else {}

	override def warn(format: String, arguments: AnyRef*) = if(isWarnEnabled) {
		logger.warn(MessageFormatter.arrayFormat(format, arguments.toArray).getMessage)
	} else {}

	override def warn(format: String, arg: scala.Any) = if(isWarnEnabled) {
		logger.warn(MessageFormatter.format(format, arg).getMessage)
	} else {}

	override def warn(msg: String) = if(isWarnEnabled) {
		logger.warn(msg)
	} else {}

	override def isWarnEnabled = logger.isWarnEnabled

	override def info(marker: slf4j.Marker, msg: String, t: Throwable) = if(isInfoEnabled(marker)) {
		logger.info(convertMarker(marker), msg, t)
	} else {}

	override def info(marker: slf4j.Marker, format: String, arguments: AnyRef*) = if(isInfoEnabled(marker)) {
		logger.info(convertMarker(marker), MessageFormatter.arrayFormat(format, arguments.toArray).getMessage)
	} else {}

	override def info(marker: slf4j.Marker, format: String, arg1: scala.Any, arg2: scala.Any) = if(isInfoEnabled(marker)) {
		logger.info(convertMarker(marker), MessageFormatter.format(format, arg1, arg2).getMessage)
	} else {}

	override def info(marker: slf4j.Marker, format: String, arg: scala.Any) = if(isInfoEnabled(marker)) {
		logger.info(convertMarker(marker), MessageFormatter.format(format, arg).getMessage)
	} else {}

	override def info(marker: slf4j.Marker, msg: String) = if(isInfoEnabled(marker)) {
		logger.info(convertMarker(marker), msg)
	} else {}

	override def isInfoEnabled(marker: slf4j.Marker) = logger.isInfoEnabled(convertMarker(marker))

	override def info(msg: String, t: Throwable) = if(isInfoEnabled) {
		logger.info(msg, t)
	} else {}

	override def info(format: String, arguments: AnyRef*) = if(isInfoEnabled) {
		logger.info(MessageFormatter.arrayFormat(format, arguments.toArray).getMessage)
	} else {}

	override def info(format: String, arg1: scala.Any, arg2: scala.Any) = if(isInfoEnabled) {
		logger.info(MessageFormatter.format(format, arg1, arg2).getMessage)
	} else {}

	override def info(format: String, arg: scala.Any) = if(isInfoEnabled) {
		logger.info(MessageFormatter.format(format, arg).getMessage)
	} else {}

	override def info(msg: String) = if(isInfoEnabled) {
		logger.info(msg)
	} else {}

	override def isInfoEnabled = logger.isInfoEnabled

	override def debug(marker: slf4j.Marker, msg: String, t: Throwable) = if(isDebugEnabled(marker)) {
		logger.debug(convertMarker(marker), msg, t)
	} else {}

	override def debug(marker: slf4j.Marker, format: String, arguments: AnyRef*) = if(isDebugEnabled(marker)) {
		logger.debug(convertMarker(marker), MessageFormatter.arrayFormat(format, arguments.toArray).getMessage)
	} else {}

	override def debug(marker: slf4j.Marker, format: String, arg1: scala.Any, arg2: scala.Any) = if(isDebugEnabled(marker)) {
		logger.debug(convertMarker(marker), MessageFormatter.format(format, arg1, arg2).getMessage)
	} else {}

	override def debug(marker: slf4j.Marker, format: String, arg: scala.Any) = if(isDebugEnabled(marker)) {
		logger.debug(convertMarker(marker), MessageFormatter.format(format, arg).getMessage)
	} else {}

	override def debug(marker: slf4j.Marker, msg: String) = if(isDebugEnabled(marker)) {
		logger.debug(convertMarker(marker), msg)
	} else {}

	override def isDebugEnabled(marker: slf4j.Marker) = logger.isDebugEnabled(convertMarker(marker))

	override def debug(msg: String, t: Throwable) = if(isDebugEnabled) {
		logger.debug(msg, t)
	} else {}

	override def debug(format: String, arguments: AnyRef*) = if(isDebugEnabled) {
		logger.debug(MessageFormatter.arrayFormat(format, arguments.toArray).getMessage)
	} else {}

	override def debug(format: String, arg1: scala.Any, arg2: scala.Any) = if(isDebugEnabled) {
		logger.debug(MessageFormatter.format(format, arg1, arg2).getMessage)
	} else {}

	override def debug(format: String, arg: scala.Any) = if(isDebugEnabled) {
		logger.debug(MessageFormatter.format(format, arg).getMessage)
	} else {}

	override def debug(msg: String) = if(isDebugEnabled) {
		logger.debug(msg)
	} else {}

	override def isDebugEnabled = logger.isDebugEnabled

	override def trace(marker: slf4j.Marker, msg: String, t: Throwable) = if(isTraceEnabled(marker)) {
		logger.trace(convertMarker(marker), msg, t)
	} else {}

	override def trace(marker: slf4j.Marker, format: String, argArray: AnyRef*) = if(isTraceEnabled(marker)) {
		logger.trace(convertMarker(marker), MessageFormatter.arrayFormat(format, argArray.toArray).getMessage)
	} else {}

	override def trace(marker: slf4j.Marker, format: String, arg1: scala.Any, arg2: scala.Any) = if(isTraceEnabled(marker)) {
		logger.trace(convertMarker(marker), MessageFormatter.format(format, arg1, arg2).getMessage)
	} else {}

	override def trace(marker: slf4j.Marker, format: String, arg: scala.Any) = if(isTraceEnabled(marker)) {
		logger.trace(convertMarker(marker), MessageFormatter.format(format, arg).getMessage)
	} else {}

	override def trace(marker: slf4j.Marker, msg: String) = if(isTraceEnabled(marker)) {
		logger.trace(convertMarker(marker), msg)
	} else {}

	override def isTraceEnabled(marker: slf4j.Marker) = logger.isTraceEnabled(convertMarker(marker))

	override def trace(msg: String, t: Throwable) = if(isTraceEnabled) {
		logger.trace(msg, t)
	} else {}

	override def trace(format: String, arguments: AnyRef*) = if(isTraceEnabled) {
		logger.trace(MessageFormatter.arrayFormat(format, arguments.toArray).getMessage)
	} else {}

	override def trace(format: String, arg1: scala.Any, arg2: scala.Any) = if(isTraceEnabled) {
		logger.trace(MessageFormatter.format(format, arg1, arg2).getMessage)
	} else {}

	override def trace(format: String, arg: scala.Any) = if(isTraceEnabled) {
		logger.trace(MessageFormatter.format(format, arg).getMessage)
	} else {}

	override def trace(msg: String) = logger.trace(msg)

	override def isTraceEnabled = logger.isTraceEnabled

	override def getName = logger.getName
}