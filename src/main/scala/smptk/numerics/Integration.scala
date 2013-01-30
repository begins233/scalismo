package smptk.numerics

import smptk.image._
import breeze.linalg.DenseVector

object Integration {
     implicit val numSamples : Int = 100
  
	def integrate[Point <: CoordVectorLike](img : ContinuousScalarImageLike[Point])(implicit numSamples : Int) : Float = { 
  	    val domain = img.domain
	    val sampleValues : IndexedSeq[Float] = domain.uniformSamples(numSamples).map(img)
	   
	    val sum = sampleValues.sum
	    var ndVolume = 1f;  	    
	    for (d <- 0 until domain.dimensionality) {
	      ndVolume = domain.extent(d) * ndVolume
	    }
	   	    
	    sum * ndVolume / numSamples.toFloat
	}
     
       
	def integrate[Point <: CoordVectorLike](img : ContinuousVectorImageLike[Point])(implicit numSamples : Int) : DenseVector[Float] = { 
  	    val domain = img.domain
	    val sampleValues : IndexedSeq[DenseVector[Float]] = domain.uniformSamples(numSamples).map(img)
	    
	    var ndVolume = 1f;
	     for (d <- 0 until domain.dimensionality) {
	      ndVolume = domain.extent(d) * ndVolume
	    }
	    
	    val zeroVector = DenseVector.zeros[Float](img.pixelDimensionality)
	    val sum : DenseVector[Float] = sampleValues.foldLeft(zeroVector)(_ + _)
	    sum * ndVolume / numSamples.toFloat
	    
	}
}