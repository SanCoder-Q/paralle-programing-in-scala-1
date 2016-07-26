package scalashop

import org.scalameter._
import common._

object VerticalBoxBlurRunner {

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 5,
    Key.exec.maxWarmupRuns -> 10,
    Key.exec.benchRuns -> 10,
    Key.verbose -> true
  ) withWarmer (new Warmer.Default)

  def main(args: Array[String]): Unit = {
    val radius = 3
    val width = 1920
    val height = 1080
    val src = new Img(width, height)
    val dst = new Img(width, height)
    val seqtime = standardConfig measure {
      VerticalBoxBlur.blur(src, dst, 0, width, radius)
    }
    println(s"sequential blur time: $seqtime ms")

    val numTasks = 32
    val partime = standardConfig measure {
      VerticalBoxBlur.parBlur(src, dst, numTasks, radius)
    }
    println(s"fork/join blur time: $partime ms")
    println(s"speedup: ${seqtime / partime}")
  }

}

/** A simple, trivially parallelizable computation. */
object VerticalBoxBlur {

  /** Blurs the columns of the source image `src` into the destination image
    * `dst`, starting with `from` and ending with `end` (non-inclusive).
    *
    * Within each column, `blur` traverses the pixels by going from top to
    * bottom.
    */
  def blur(src: Img, dst: Img, from: Int, end: Int, radius: Int): Unit = {
    // TODO implement this method using the `boxBlurKernel` method
    val cors = for (x <- from until end; y <- 0 until src.height) yield (x, y)
    cors.foreach {
      case (x, y) => dst(x, y) = boxBlurKernel(src, x, y, radius)
    }
  }

  /** Blurs the columns of the source image in parallel using `numTasks` tasks.
    *
    * Parallelization is done by stripping the source image `src` into
    * `numTasks` separate strips, where each strip is composed of some number of
    * columns.
    */
  def parBlur(src: Img, dst: Img, numTasks: Int, radius: Int): Unit = {
    // TODO implement using the `task` construct and the `blur` method
    val step = src.width / numTasks // 0
    val offset = src.width % numTasks // 3
    val tasks = if (step > 0)
      (0 until numTasks).map(i =>
        if (i < offset)
          task {
            blur(src, dst, (step + 1) * i, Math.min((step + 1) * (i + 1), src.width), radius)
          }
        else
          task {
            blur(src, dst, step * i + offset, Math.min(step * (i + 1) + offset, src.width), radius)
          }
      )
    else
      (0 until offset).map(i =>
        task {
          blur(src, dst, i, i + 1, radius)
        }
      )
    tasks.foreach(_.join())
  }

}
