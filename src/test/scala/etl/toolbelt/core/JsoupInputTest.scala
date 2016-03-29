package etl.toolbelt.core

import etl.toolbelt.base.EtlToolbeltSpec

class JsoupInputTest extends EtlToolbeltSpec{

  "The sitemap" should "only return values for certain level" in {
    val jsoup = new JsoupInput {
      override val agent: String = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6"
      override val suffix: String = ""
      override val prefix: String = "www"
      override val host: String = "localhost"
      override val protocol: String = "http://"
    }

    val siteMap = scala.collection.mutable.HashMap.empty[String, (String, Int)]

    siteMap.put("http://localhost/link1", ("http://localhost", 0))
    siteMap.put("http://localhost/link2", ("http://localhost", 0))
    siteMap.put("http://localhost/link11", ("http://localhost/link1", 1))
    val l = jsoup.getLinks(1,siteMap)
    l.keys.count(p => true) should equal (1)
  }

  "The sitemap" should "not have levels in 3" in {
    val jsoup = new JsoupInput {
      override val agent: String = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6"
      override val suffix: String = ""
      override val prefix: String = "www"
      override val host: String = "localhost"
      override val protocol: String = "http://"
    }

    val siteMap = scala.collection.mutable.HashMap.empty[String, (String, Int)]

    siteMap.put("http://localhost/link1", ("http://localhost", 0))
    siteMap.put("http://localhost/link2", ("http://localhost", 0))
    siteMap.put("http://localhost/link11", ("http://localhost/link1", 1))
    siteMap.put("http://localhost/link111", ("http://localhost/link11", 2))
    val l = jsoup.hasLevel(siteMap,3)
    l should equal (false)
  }

}
