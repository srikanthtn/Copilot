---
description: generate-documentaion
---

# Documentation Generation Workflow 

## Target Agent
Google Antigravity

## Project Root
`<project-root>`

## Objective
Generate a **complete, developer-ready documentation suite** for the project, covering:

- Application purpose and architecture
- Backend services and API contracts
- AI / Agent workflows (if applicable)
- UI component references
- Setup, environment, and operational guidance

All documentation must be:
- Inferred directly from the codebase
- Accurate and implementation-driven
- Suitable for onboarding new developers

---

## 🏗 Phase 1: Context & Architecture Scoping

Before generating documentation, the agent must establish a **clear mental model** of the system.

### Antigravity Prompt
> Scan the project root and analyze core entry files such as:
> - Application bootstrap files (e.g., `App.tsx`, `main.ts`, `index.tsx`)
> - Configuration files (`package.json`, build configs)
>
> Identify:
> - The core technology stack (frameworks, language, tooling)
> - The primary purpose of the application
> - Key architectural patterns (SPA, SSR, micro-frontend, API-driven UI, etc.)
>
> Produce a concise architectural overview describing:
> - What the system does
> - How it is structured
> - How major parts interact

### Output Target
`docs/01_OVERVIEW.md`

---

## 🔌 Phase 2: Service & API Layer Documentation

This phase documents backend-facing logic, services, APIs, and data structures.

### Steps

#### 1. Service Analysis
- **Target:** `services/`, `lib/`, or equivalent directories
- **Directive:**
  > For each service/module:
  > - Explain its responsibility
  > - Identify external APIs, internal modules, or data stores it interacts with
  > - Describe error-handling strategies (try/catch, fallbacks, retries, guards)

#### 2. Data Schema Mapping
- **Target:** JSON files, database seeds, mock data, or schema definitions
- **Directive:**
  > Infer data models (TypeScript interfaces or schema definitions).
  > For each field:
  > - Describe its purpose
  > - Specify inferred data type
  > - Clarify ambiguous or overloaded fields

#### 3. API Endpoint Documentation
- **Target:** `api/`, `routes/`, `controllers/`, or similar directories
- **Directive:**
  > Document all exposed endpoints, including:
  > - Endpoint path
  > - HTTP method
  > - Required parameters
  > - Request and response shapes
  > - Error scenarios (where identifiable)

### Output Target
`docs/02_BACKEND_API.md`

---

## 🤖 Phase 3: AI / Agent Logic Documentation (Optional)

Apply this phase only if the project contains AI, agents, or automation logic.

### Antigravity Prompt
> Analyze AI-related folders (e.g., `.agent`, `ai/`, `models/`, `pipelines/`).
>
> Document:
> 1. **Model Registry**
>    - List configured models
>    - Describe configuration parameters and intended usage
>
> 2. **Agent / Pipeline Workflow**
>    - Explain triggers (file upload, API call, scheduled run, etc.)
>    - Describe processing steps
>    - Identify inputs, outputs, and storage locations
>
> Use diagrams or step-by-step flows where helpful.

### Output Target
`docs/03_AI_AGENTS.md`  
*(Skip if not applicable)*

---

## ⚛️ Phase 4: UI Component Reference

This phase generates a component-level reference for frontend developers.

### Antigravity Prompt
> Traverse UI component directories (e.g., `components/`, `ui/`, `pages/`).
>
> For each component:
> 1. Extract and document the Props interface/type
> 2. Describe what the component renders and its responsibility
> 3. Identify where it is used (pages, layouts, parent components)
>
> Group related components when appropriate.

### Output Target
`docs/04_UI_COMPONENTS.md`

---

## 🚀 Phase 5: Setup & Operations Guide

This phase documents environment configuration and operational workflows.

### Antigravity Prompt
> Review:
> - Environment files (`.env*`) — keys only
> - `.gitignore`
> - `scripts/` or automation folders
>
> Document:
> 1. **Prerequisites**
>    - Required runtimes (Node, Python, etc.)
>    - Required environment variables or API keys
> 2. **Runtime Folders**
>    - Purpose of temporary or generated directories
>    - Cleanup and maintenance guidance
> 3. **Scripts**
>    - Purpose of custom scripts
>    - How and when they should be used

### Output Target
`docs/05_SETUP_GUIDE.md`

---

## 🏁 Final Phase: Documentation Index

Create a single entry point for all generated documentation.

### Antigravity Prompt
> Create a root-level `documentation_index.md` that:
> - Links to all generated documentation files
> - Includes a short introduction for new developers
> - Explains how to navigate and use the documentation set

### Final Output
`documentation_index.md`

---

## Documentation Quality Rules
- Infer information directly from code — no assumptions
- Avoid speculation
- Prefer clarity over verbosity
- Use consistent headings and structure
- Write for **developers new to the project**
