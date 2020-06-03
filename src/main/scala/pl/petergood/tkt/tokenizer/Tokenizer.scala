package pl.petergood.tkt.tokenizer

trait MessageTokenizer {
  def tokenize(s: String): Array[String]
}

object Tokenizer {
  def apply(): MessageTokenizer = {
    new SimpleTokenizer()
  }

  private class SimpleTokenizer extends MessageTokenizer {
    private val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ".toSet

    def tokenize(s: String): Array[String] = {
      s.filter(chars).split(" ")
    }
  }
}
