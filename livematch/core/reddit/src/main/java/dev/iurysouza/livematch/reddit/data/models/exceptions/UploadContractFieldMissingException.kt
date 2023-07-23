package dev.iurysouza.livematch.reddit.data.models.exceptions

class UploadContractFieldMissingException(field: String) :
  Exception("The field '$field' was not found while creating UploadData!")
