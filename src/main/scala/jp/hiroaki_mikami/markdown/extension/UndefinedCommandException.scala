package jp.hiroaki_mikami.markdown.extension

/**
 * Created by mikami on 14/12/15.
 */
class UndefinedCommandException(commandName: String) extends Exception(commandName + " is not defined.")