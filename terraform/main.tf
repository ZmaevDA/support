locals {
  keycloak_client_id    = data.terraform_remote_state.global.outputs.keycloak_client_id
  keycloak_realm_name   = data.terraform_remote_state.global.outputs.keycloak_realm_name
  minikube_uri          = data.terraform_remote_state.global.outputs.minikube_ip
  keycloak_port         = data.terraform_remote_state.global.outputs.keycloak_port
  oauth_security_schema = data.terraform_remote_state.global.outputs.oauth_security_schema

  env_vars = {
    "KEYCLOAK_CLIENT_ID"            = local.keycloak_client_id
    "KEYCLOAK_CLIENT_SECRET"        = data.terraform_remote_state.global.outputs.keycloak_client_secret
    "DB_HOST"                       = data.terraform_remote_state.global.outputs.db_service_host
    "DB_PORT"                       = "5432"
    "DB_NAME"                       = data.terraform_remote_state.global.outputs.db_name
    "DB_USERNAME"                   = data.terraform_remote_state.global.outputs.db_user
    "DB_PASSWORD"                   = data.terraform_remote_state.global.outputs.db_password
    "HEADER_SECRET_KEY"             = "123"
    "OAUTH_SECURITY_SCHEMA"         = local.oauth_security_schema
    "KEYCLOAK_URI"                  = local.minikube_uri
    "ELASTICSEARCH_URI"             = data.terraform_remote_state.global.outputs.elasticsearch_service_host
    "RABBITMQ_URI"                  = local.minikube_uri
    "RABBITMQ_PORT"                 = data.terraform_remote_state.global.outputs.rabbitmq_service_amqp_port
    "KEYCLOAK_PORT"                 = local.keycloak_port
    "SERVICE_URI"                   = "http://localhost:8082"
    "KEYCLOAK_REALM_NAME"           = local.keycloak_realm_name
    "KEYCLOAK_ADMIN_USERNAME"       = "Admin"
    "KEYCLOAK_ADMIN_PASSWORD"       = data.terraform_remote_state.global.outputs.keycloak_db_admin_password
    "CLIENT_SECRET"                 = data.terraform_remote_state.global.outputs.keycloak_client_secret
    "OTEL_SERVICE_NAME"             = "support"
    "OTEL_EXPORTER_OTLP_ENDPOINT"   = "http://${local.minikube_uri}:31001"
    "OTEL_EXPORTER_OTLP_PROTOCOL"   = "grpc"
    "OTEL_RESOURCE_ATTRIBUTES"      = "service.name=support,service.instance.id=support,env=dev"
    "OTEL_LOGS_EXPORTER"            = "otlp"
    "OTEL_METRIC_EXPORT_INTERVAL"   = "500"
    "OTEL_BSP_SCHEDULE_DELAY"       = "500"
  }
}

data "terraform_remote_state" "global" {
  backend = "local"

  config = {
    path = "/home/dany0k/redcollar/infra/terraform/terraform.tfstate"
  }
}

resource "kubernetes_deployment" "support_service" {
  metadata {
    name      = "support-service"
    namespace = data.terraform_remote_state.global.outputs.namespace_name
  }

  spec {
    replicas = 1
    selector {
      match_labels = {
        app = "support-service"
      }
    }

    template {
      metadata {
        labels = {
          app = "support-service"
        }
      }

      spec {
        container {
          name  = "support-service"
          image = "zhmash/support-service:latest"

          dynamic "env" {
            for_each = local.env_vars
            content {
              name  = env.key
              value = env.value
            }
          }

          port {
            container_port = 8081
          }

          resources {
            limits = {
              memory = "512Mi"
            }
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "support_service_service" {
  metadata {
    name      = "support-service-service"
    namespace = data.terraform_remote_state.global.outputs.namespace_name
  }

  spec {
    selector = {
      app = "support-service"
    }

    port {
      port        = 8081
      target_port = 8081
      node_port   = 30010
    }
    type = "NodePort"
  }
}