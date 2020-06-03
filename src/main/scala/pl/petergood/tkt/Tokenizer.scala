package pl.petergood.tkt

object Tokenizer {
  private val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ".toSet

  def tokenize(s: String): Array[String] = {
    s.filter(chars).split(" ")
  }
}
