package etl.toolbelt.core

import java.net.URL
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import scala.collection.mutable
import scala.util.{Failure, Success, Try}
import scala.collection.JavaConversions._

abstract class JsoupInput {

  val protocol: String
  val prefix: String
  val suffix: String
  val host: String
  val agent: String

  /**
    * Composes the URL to crawl
    *
    * @return a string with the concatenated URL
    */

  def url: String = s"$protocol$prefix.$host/$suffix"

  /**
    * Returns a Jsoup document for the default URL
    *
    * @return an object that contains a Jsoup document and a message text
    */

  def document: Either[Document, String] = {
      document(url)
  }

  /**
    * Returns a Jsoup document for a given URL
    *
    * @param u URL
    * @return an object that contains a Jsoup document and a message text
    */

  def document(u: String): Either[Document, String] = {
    Try{
      Jsoup.connect(u).userAgent(agent).get()
    } match {
      case Success(d) => Left(d)
      case Failure(e) => Right(s"Error loading document: ${e.getMessage()}")
    }
  }

  /**
    * All links for the default URL
    *
    * @return a list of URLs and a message
    */

  def allLinks: Either[List[String], String] = {
    allLinks(url)
  }

  /**
    * All links for a given URL
    *
    * @param u URL
    * @return a list of URLs and a message
    */

  def allLinks(u: String): Either[List[String], String] = {
    document(u) match {
      case Left(d) => {
        val links = d.select("a[href]").iterator.toList.map(p => p.attr("abs:href"))
        val filteredLinks = links.filter(q => domain(q) == prefix + "." + host)
        Left(filteredLinks)
      }
      case Right(e) => Right(e)
    }
  }

  /**
    * Returns the host for a specific URL
    *
    * @param url URL
    * @return host
    */

  def domain(url: String): String = {
    Try{
      val u = new URL(url)
      u.getHost()
    }
    match {
      case Success(u) => u
      case Failure(e) => ""
    }

  }

  /**
    * Generates the site map recursively for a site
    *
    * @param sMap Sitemap hashmap
    * @param f Function to get all links
    * @param url URL
    * @param depth How many levels down the sitemap to go
    * @return Sitemap hashmap
    */

  def generateSiteMap(sMap: mutable.HashMap[String, (String, Int)], f: String => Either[List[String], String],
                      url: String, depth: Int = -1): mutable.HashMap[String, (String, Int)] = {


    def updateSiteMap(sMap: mutable.HashMap[String, (String, Int)], f: String => Either[List[String], String],
                      lvl: Int): mutable.HashMap[String, (String, Int)] = {

      val cond = if (depth == -1){
        hasLevel(sMap, lvl)
      }
      else {
        hasLevel(sMap, lvl) && lvl < depth
      }

      if (cond) {
        val linksList = if (lvl == -1) Iterable(url) else getLinks(lvl, sMap).keys

        val sMapTmp = linksList.map(p => (f(p), p))

        sMapTmp.foreach {
          q => q._1 match {
            case Left(l) => {
              l.foreach(p => sMap.getOrElseUpdate(p, (q._2, lvl + 1)))
            }
            case Right(e) => e
          }
        }
        updateSiteMap(sMap, f, lvl + 1)

      }
      else {
        sMap
      }
    }

    updateSiteMap(sMap, f, -1)

  }

  def getLinks(lvl: Int, sMap: mutable.HashMap[String, (String, Int)]): mutable.HashMap[String, (String, Int)] = {
    sMap.filter(p => p._2._2 == lvl)
  }

  def hasLevel(sMap: mutable.HashMap[String, (String, Int)], lvl: Int): Boolean = {
    if (lvl == -1) true else sMap.exists(p => p._2._2 == lvl)
  }

}
