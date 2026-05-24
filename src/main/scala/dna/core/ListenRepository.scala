package dna.core

import dna.domain.ListenEntry

trait ListenRepository[F[_]]:
  def saveAll(listens: List[ListenEntry]): F[Unit]
  def readAll(): F[List[ListenEntry]]
