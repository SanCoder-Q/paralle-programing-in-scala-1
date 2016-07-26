
import common._

import scala.collection.immutable.IndexedSeq

package object scalashop {

  /** The value of every pixel is represented as a 32 bit integer. */
  type RGBA = Int

  /** Returns the red component. */
  def red(c: RGBA): Int = (0xff000000 & c) >>> 24

  /** Returns the green component. */
  def green(c: RGBA): Int = (0x00ff0000 & c) >>> 16

  /** Returns the blue component. */
  def blue(c: RGBA): Int = (0x0000ff00 & c) >>> 8

  /** Returns the alpha component. */
  def alpha(c: RGBA): Int = (0x000000ff & c) >>> 0

  /** Used to create an RGBA value from separate components. */
  def rgba(r: Int, g: Int, b: Int, a: Int): RGBA = {
    (r << 24) | (g << 16) | (b << 8) | (a << 0)
  }

  /** Restricts the integer into the specified range. */
  def clamp(v: Int, min: Int, max: Int): Int = {
    if (v < min) min
    else if (v > max) max
    else v
  }

  /** Image is a two-dimensional matrix of pixel values. */
  class Img(val width: Int, val height: Int, private val data: Array[RGBA]) {
    def this(w: Int, h: Int) = this(w, h, new Array(w * h))

    def apply(x: Int, y: Int): RGBA = data(y * width + x)

    def update(x: Int, y: Int, c: RGBA): Unit = data(y * width + x) = c
  }

  /** Computes the blurred RGBA value of a single pixel of the input image. */
  def boxBlurKernel(src: Img, x: Int, y: Int, radius: Int): RGBA = {
    // TODO implement using while loops
    var xx = clamp(x - radius, 0, src.width - 1)
    var yy = clamp(y - radius, 0, src.height - 1)
    var count = 0
    var sr = 0; var sg = 0; var sb = 0; var sa = 0

    while (xx < clamp(x + radius + 1, 0, src.width)) {
      while (yy < clamp(y + radius + 1, 0, src.height)) {
        val pixel: RGBA = src(xx, yy)
        sr = sr + red(pixel)
        sg = sg + green(pixel)
        sb = sb + blue(pixel)
        sa = sa + alpha(pixel)
        count = count + 1
        yy = yy + 1
      }
      yy = clamp(y - radius, 0, src.height - 1)
      xx = xx + 1
    }

    rgba(clamp(sr / count, 0, 255), clamp(sg / count, 0, 255), clamp(sb / count, 0, 255), clamp(sa / count, 0, 255))
//    val xr = clamp(x - radius, 0, src.width - 1) until clamp(x + radius + 1, 0, src.width)
//    val yr = clamp(y - radius, 0, src.height - 1) until clamp(y + radius + 1, 0, src.height)
//    val cors = for (x <- xr; y <- yr) yield (x, y)
//    val (sr, sg, sb, sa) = cors.map { case (x, y) => {
//      val pixel: RGBA = src(x, y)
//      (red(pixel), green(pixel), blue(pixel), alpha(pixel))
//    }}.foldLeft(0, 0, 0, 0) {
//      case ((sr, sg, sb, sa), (r, g, b, a)) => (sr + r, sg + g, sb + b, sa + a)
//    }
//    rgba(sr / cors.length, sg / cors.length, sb / cors.length, sa / cors.length)
  }

}
