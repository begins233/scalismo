package org.statismo.stk.core.numerics

import org.statismo.stk.core.image._
import breeze.linalg.DenseVector
import org.statismo.stk.core.geometry._


case class IntegratorConfiguration[D <: Dim](sampler: Sampler[D])

case class Integrator[D <: Dim: NDSpace](configuration: IntegratorConfiguration[D]) {

  def sampler = configuration.sampler

  def integrateScalar(img: ScalarImage[D]): Float = {
    integrateScalar(img.liftValues)
  }

  def integrateScalar(f: Function1[Point[D], Option[Float]]): Float = {
    val samples = configuration.sampler.sample

    val sum = samples.par.map { case (pt, p) => f(pt).getOrElse(0f) * (1f / p.toFloat)}.sum

    sum / samples.size
  }

  def integrateVector(img: VectorImage[D]): Vector[D] = {
    integrateVector(img.liftValues)
  }

  def integrateVector(f: Function1[Point[D], Option[Vector[D]]]): Vector[D] = {
    val samples = configuration.sampler.sample

    val zeroVector = Vector.zeros[D]
    val sum = samples.par.map { case (pt, p) => f(pt).getOrElse(zeroVector) * (1f / p.toFloat) }.foldLeft(zeroVector)((a, b) => { a + b })
    sum * (1f / (sampler.numberOfPoints - 1).toFloat)
  }

  def integrateVector(f: Function1[Point[D], Option[DenseVector[Float]]], dimensionality: Int): DenseVector[Float] = {
    val samples = configuration.sampler.sample

    val zeroVector = DenseVector.zeros[Float](dimensionality)
    val sum = samples.par.map { case (pt, p) => f(pt).getOrElse(zeroVector) * (1f / p.toFloat) }.foldLeft(zeroVector)((a, b) => { a + b })
    sum * (1f / (sampler.numberOfPoints - 1).toFloat)
  }

}


