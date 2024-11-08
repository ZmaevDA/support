terraform {
  required_providers {
    yandex = {
      source = "yandex-cloud/yandex"
    }
    kubernetes = {
      source = "hashicorp/kubernetes"
      version = "2.32.0"
    }
  }
  required_version = ">= 0.13"
}

provider "yandex" {
  zone = "ru-central1-a"
}

provider "kubernetes" {
  config_path    = "~/.kube/config"
  config_context = "minikube"
}
